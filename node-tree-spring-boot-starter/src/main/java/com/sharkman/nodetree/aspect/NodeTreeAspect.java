package com.sharkman.nodetree.aspect;

import com.sharkman.nodetree.annotation.NodeTree;
import com.sharkman.nodetree.annotation.RootID;
import com.sharkman.nodetree.annotation.RootPID;
import com.sharkman.nodetree.core.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
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
        List<Object> returnList = (List<Object>) returnValue;
        if (returnList.isEmpty()) {
            return returnValue;
        }
        // 扩展点，可自定义树构造器，增加额外的行为
        TreeCreator creator = DefaultTreeCreator.getInstance();
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

    /**
     * 查看{@link NodeTree} 注解是否赋值根节点条件信息
     *
     * @param returnList 返回值
     * @param nodeTree   {@link NodeTree}
     * @param creator    数构造器{@link TreeCreator}
     * @return 结果
     */
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

    /**
     * 判断方法参数
     * @param returnList 返回值
     * @param joinPoint 切点
     * @param creator 树创建器
     * @return 构造结果
     */
    private JudgeReturnResult judgeOfParamsValue(
            List<Object> returnList, ProceedingJoinPoint joinPoint, TreeCreator creator) {
        // 方法参数
        // 如果参数为空，则直接返回所有根节点
        Object[] params = joinPoint.getArgs();
        if (null == params || 0 == params.length) {
            return new JudgeReturnResult(creator.buildTree(returnList));
        }
        // 查找方法参数上是否有注解
        JudgeReturnResult param = findResultForPrimitiveArgs(returnList, joinPoint, creator);
        if (param != null) {
            return param;
        }
        // 查找方法参数内部是否有注解
        JudgeReturnResult rootIDObj = findResultForObjectArgs(returnList, joinPoint, creator);
        if (rootIDObj != null) {
            return rootIDObj;
        }
        // 都没有，返回所有可能根节点
        return new JudgeReturnResult();
    }

    private JudgeReturnResult findResultForPrimitiveArgs(
            List<Object> returnList, ProceedingJoinPoint joinPoint, TreeCreator creator) {
        //获取方法，此处可将signature强转为MethodSignature
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Annotation[][] annotations = method.getParameterAnnotations();
        // 方法参数
        Object[] params = joinPoint.getArgs();
        for (int i = 0; i < annotations.length; i++) {
            Annotation[] curAnnotations = annotations[i];
            Object param = params[i];
            for (Annotation annotation : curAnnotations) {
                // 找 Pid（更常用）
                if (annotation.annotationType() == RootPID.class) {
                    return new JudgeReturnResult(creator.buildTreeOfRootPIdForList(
                            returnList, Optional.ofNullable(param).map(Object::toString).orElse(null)
                    ));
                }
                // 找id
                if (annotation.annotationType() == RootID.class) {
                    if (null == param) {
                        throw new NullPointerException("RootID 为空！");
                    }
                    return JudgeReturnResult.from(
                            creator.buildTreeOfRootId(returnList, param.toString())
                    );
                }

            }
        }
        return null;
    }

    private JudgeReturnResult findResultForObjectArgs(
            List<Object> returnList, ProceedingJoinPoint joinPoint, TreeCreator creator) {
        Object[] params = joinPoint.getArgs();
        // 若方法参数是对象，则遍历对象中的参数
        for (Object param : params) {
            // 如果是基本类型或字符串
            if (null == param || ReflectUtil.isPrimitiveOrWrapper(param.getClass()) || param.getClass() == String.class) {
                continue;
            }
            Class<?> clazz = param.getClass();
            // 找pid注解
            Field field = ReflectUtil.findColumnByAnnotation(param.getClass().getDeclaredFields(), RootPID.class);
            if (null != field) {
                Method rootPIDGetter;
                try {
                    rootPIDGetter = ReflectUtil.refGetMethod(clazz, field.getName());
                } catch (NoSuchMethodException e) {
                    throw new IllegalArgumentException(
                            ReflectUtil.methodNotFoundByAnnotationMessage(RootPID.class.getName(), field));
                }
                Object rootIDObj = ReflectUtil.methodInvoke(rootPIDGetter, param);
                return new JudgeReturnResult(creator.buildTreeOfRootPIdForList(
                        returnList, Optional.ofNullable(rootIDObj).map(Object::toString).orElse(null)
                ));
            }
            // 找id注解
            field = ReflectUtil.findColumnByAnnotation(param.getClass().getDeclaredFields(), RootID.class);
            if (null != field) {
                Method rootIDGetter;
                try {
                    rootIDGetter = ReflectUtil.refGetMethod(clazz, field.getName());
                } catch (NoSuchMethodException e) {
                    throw new IllegalArgumentException(
                            ReflectUtil.methodNotFoundByAnnotationMessage(RootID.class.getName(), field));
                }
                String rootID = (String) Optional.ofNullable(ReflectUtil.methodInvoke(rootIDGetter, param))
                        .orElseThrow(() -> new NullPointerException("RootID 为空!"));
                return JudgeReturnResult.from(
                        creator.buildTreeOfRootId(returnList, rootID)
                );
            }

        }
        return null;
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
