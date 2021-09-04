package com.sharkman.nodetree.aspect;

import com.sharkman.nodetree.annotation.NodeTree;
import com.sharkman.nodetree.annotation.RootID;
import com.sharkman.nodetree.annotation.RootPID;
import com.sharkman.nodetree.core.Treeable;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 节点树切面
 * 用于树节点基于 {@link com.sharkman.nodetree.annotation.NodePID}
 * {@link com.sharkman.nodetree.annotation.NodeID}实现
 *
 * @author yanpengyu
 * 2021/5/6 2:58 PM
 * @since 1.1.3
 */
@Slf4j
@Aspect
public class NodeTreeAspect {
    /**
     * 解析方法返回值，生成树结构;
     *
     * @param joinPoint 切点代理方法
     * @param nodeTree  节点树注解
     * @throws Throwable 方法执行异常，则抛出异常
     */
    @Around(value = "@annotation(nodeTree)")
    public Object constructTree(ProceedingJoinPoint joinPoint, NodeTree nodeTree) throws Throwable {
        Object returnValue = joinPoint.proceed();
        if (null == returnValue) {
            return null;
        }
        if (!(returnValue instanceof List)) {
            log.error("不支持的类型：{}, 期望类型为 List ", returnValue.getClass().getName());
            return returnValue;
        }
        @SuppressWarnings("unchecked")
        List<Object> returnList = (List) returnValue;
        if (returnList.isEmpty()) {
            return returnValue;
        }
        Object childNode = returnList.get(0);
        TreeCreator creator;
        // 如果实现了 Treeable 接口
        if (childNode instanceof Treeable) {
            creator = TreeCreatorForInterface.getInstance();
        } else {
            creator = TreeCreatorForAnnotation.getInstance();
        }
        // 首先查看 NodeTree 节点本身是否传入了固定值
        JudgeReturnResult returnResult = judgeOfNodeTypeValue(returnList, nodeTree, creator);
        if (returnResult.find) {
            return returnResult.results;
        }
        // 查找参数是否有参数注解
        // 参数对象
        returnResult = judgeOfParamsValue(returnList, joinPoint, creator);
        if (returnResult.find) {
            return returnResult.results;
        }
        // 如果没有指定计算类型，则返回所有可能节点
        return creator.buildTree(returnList);
    }


    private JudgeReturnResult judgeOfNodeTypeValue(
            List<Object> returnList, NodeTree nodeTree, TreeCreator creator) {
        if (!"".equals(nodeTree.id())) {
            return JudgeReturnResult.from(creator.buildTreeOfRootId(returnList, nodeTree.id()));
        }
        if (!"".equals(nodeTree.pid())) {
            return new JudgeReturnResult(creator.buildTreeOfRootPIdForList(
                    returnList, nodeTree.pid()
            ));
        }
        if (nodeTree.isPidNull()) {
            return new JudgeReturnResult(creator.buildTreeOfRootPIdForList(
                    returnList, null
            ));
        }
        return new JudgeReturnResult();
    }

    private JudgeReturnResult judgeOfParamsValue(
            List<Object> returnList, ProceedingJoinPoint joinPoint, TreeCreator creator) {
        Object[] params = joinPoint.getArgs();
        if (null == params || 0 == params.length) {
            return new JudgeReturnResult(creator.buildTree(returnList));
        }
        //获取方法，此处可将signature强转为MethodSignature
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Annotation[][] annotations = method.getParameterAnnotations();

        for (int i = 0; i < annotations.length; i++) {
            Annotation[] curAnnotations = annotations[i];
            Object param = params[i];
            for (Annotation annotation : curAnnotations) {
                if (annotation.annotationType() == RootID.class) {
                    if (null == param) {
                        throw new NullPointerException("RootID 为空！");
                    }
                    return JudgeReturnResult.from(
                            creator.buildTreeOfRootId(returnList, param.toString())
                    );
                }
                if (annotation.annotationType() == RootPID.class) {
                    return new JudgeReturnResult(creator.buildTreeOfRootPIdForList(
                            returnList, Optional.ofNullable(param).map(Object::toString).orElse(null)
                    ));
                }
            }
        }
        return new JudgeReturnResult();
    }

    private static class JudgeReturnResult {
        private boolean find;
        private List<Object> results;

        JudgeReturnResult() {
        }

        JudgeReturnResult(List<Object> results) {
            this.find = true;
            this.results = results;
        }

        // 由于 object 是List 父类，为了避免冲突，使用静态方法创建类
        static JudgeReturnResult from(Object result) {
            return new JudgeReturnResult(Collections.singletonList(result));
        }
    }
}
