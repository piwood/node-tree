package com.sharkman.nodetree.aspect;

import com.sharkman.nodetree.annotation.NodeTree;
import com.sharkman.nodetree.annotation.RootID;
import com.sharkman.nodetree.annotation.RootPID;
import com.sharkman.nodetree.core.TreeUtilForAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

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
 * @see NodeTreeAspectForInterface 用于树节点基于接口实现
 * 2021/5/6 2:58 PM
 * @since 1.1.3
 */
@Slf4j
@Aspect
@Component
@ConditionalOnProperty(name = "node-tree.type", havingValue = "annotation", matchIfMissing = true)
public class NodeTreeAspectForAnnotation {
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
        // 首先查看 NodeTree 节点本身是否传入了固定值
        JudgeReturnResult returnResult = judgeOfNodeTypeValue(returnList, nodeTree);
        if (returnResult.find) {
            return returnResult.results;
        }
        // 查找参数是否有参数注解
        // 参数对象
        returnResult = judgeOfParamsValue(returnList, joinPoint);
        if (returnResult.find) {
            return returnResult.results;
        }
        return TreeUtilForAnnotation.buildTree(returnList);
    }


    private JudgeReturnResult judgeOfNodeTypeValue(List<Object> returnList, NodeTree nodeTree) {
        if (!"".equals(nodeTree.id())) {
            return new JudgeReturnResult(TreeUtilForAnnotation.buildTreeOfRootId(returnList, nodeTree.id()));
        }
        if (!"".equals(nodeTree.pid())) {
            return new JudgeReturnResult(TreeUtilForAnnotation.buildTreeOfRootPIdForList(
                    returnList, nodeTree.pid()
            ));
        }
        if (nodeTree.isPidNull()) {
            return new JudgeReturnResult(TreeUtilForAnnotation.buildTreeOfRootPIdForList(
                    returnList, null
            ));
        }
        return new JudgeReturnResult();
    }

    private JudgeReturnResult judgeOfParamsValue(List<Object> returnList, ProceedingJoinPoint joinPoint) {
        Object[] params = joinPoint.getArgs();
        if (null == params || 0 == params.length) {
            return new JudgeReturnResult(TreeUtilForAnnotation.buildTree(returnList));
        }
        //获取方法，此处可将signature强转为MethodSignature
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Annotation[][] annotations = method.getParameterAnnotations();

        for (int i = 0; i < annotations.length; i++) {
            Annotation[] curAnnotations = annotations[i];
            Object param = params[i];
            for (Annotation annotation : curAnnotations) {
                if (annotation.annotationType().isAnnotationPresent(RootID.class)) {
                    if (null == param) {
                        throw new NullPointerException("RootID 为空！");
                    }
                    return new JudgeReturnResult(
                            TreeUtilForAnnotation.buildTreeOfRootId(returnList, param.toString())
                    );
                }
                if (annotation.annotationType().isAnnotationPresent(RootPID.class)) {
                    return new JudgeReturnResult(TreeUtilForAnnotation.buildTreeOfRootPIdForList(
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

        JudgeReturnResult(Object result) {
            this.find = true;
            this.results = Collections.singletonList(result);
        }
    }
}
