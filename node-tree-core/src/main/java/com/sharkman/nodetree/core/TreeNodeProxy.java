package com.sharkman.nodetree.core;

import com.sharkman.nodetree.annotation.NodeChildren;
import com.sharkman.nodetree.annotation.NodeID;
import com.sharkman.nodetree.annotation.NodePID;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.sharkman.nodetree.core.ReflectUtil.*;

/**
 * <p> Description:树节点反射代理</p>
 * <p> CreationTime: 2021/5/6 18:02
 * <br>Email: <a href="mailto:526478642@qq.com">526478642@qq.com</a></p>
 *
 * @author yanpengyu
 * @version 1.0
 * @since 1.0
 */
final class TreeNodeProxy<T> {
    private final Method nodeIdGetter;
    private final Method pidGetter;
    private final Method childrenGetter;
    private final Method childrenSetter;


    private TreeNodeProxy(Method nodeIdGetter,
                          Method pidGetter,
                          Method childrenGetter,
                          Method childrenSetter) {
        this.nodeIdGetter = nodeIdGetter;
        this.pidGetter = pidGetter;
        this.childrenGetter = childrenGetter;
        this.childrenSetter = childrenSetter;
    }

    static <U> TreeNodeProxy<U> from(U obj) {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        Field field = ReflectUtil.findColumnByAnnotation(fields, NodeID.class);
        if (null == field) {
            throw new IllegalArgumentException(filedNotFoundMessageByAnnotation(NodeID.class.getName()));
        }
        Method nodeIdGetter;
        try {
            nodeIdGetter = ReflectUtil.refGetMethod(clazz, field.getName());
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    methodNotFoundByAnnotationMessage(NodeID.class.getName(), field), e);
        }

        field = ReflectUtil.findColumnByAnnotation(fields, NodePID.class);
        if (null == field) {
            throw new IllegalArgumentException(filedNotFoundMessageByAnnotation(NodePID.class.getName()));
        }
        Method pidGetter;
        try {
            pidGetter = ReflectUtil.refGetMethod(clazz, field.getName());
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    methodNotFoundByAnnotationMessage(NodePID.class.getName(), field), e);
        }

        field = ReflectUtil.findColumnByAnnotation(fields, NodeChildren.class);
        if (null == field) {
            throw new IllegalArgumentException(filedNotFoundMessageByAnnotation(NodeChildren.class.getName()));
        }
        Method childrenGetter;
        Method childrenSetter;
        try {
            childrenGetter = ReflectUtil.refGetMethod(clazz, field.getName());
            childrenSetter = ReflectUtil.refSetMethod(clazz, field.getName(), List.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    methodNotFoundByAnnotationMessage(NodeChildren.class.getName(), field), e);
        }
        return new TreeNodeProxy<>(nodeIdGetter, pidGetter, childrenGetter, childrenSetter);
    }

    /**
     * 获取树节点 id
     *
     * @param targetObj 目标对象
     * @return 树节点 id
     */
    String getId(T targetObj) {
        return Optional.ofNullable(methodInvoke(nodeIdGetter, targetObj))
                .map(Object::toString)
                .orElse(null);
    }

    /**
     * 获取树节点父节点 id
     *
     * @param targetObj 目标对象
     * @return 父节点 id
     */
    String getPId(T targetObj) {
        return Optional.ofNullable(methodInvoke(pidGetter, targetObj))
                .map(Object::toString)
                .orElse(null);
    }

    /**
     * 获取子节点
     *
     * @param targetObj 目标对象
     * @return 孩子节点
     */
    List<T> getChildren(T targetObj) {
        Object children = methodInvoke(childrenGetter, targetObj);
        if (null == children) {
            return Collections.emptyList();
        }
        if (!(children instanceof List)) {
            throw new IllegalStateException("children 为非List!转化失败, 获取 children 信息失败");
        }
        @SuppressWarnings("unchecked")
        List<T> result = (List<T>) children;
        return result;

    }

    /**
     * 设置子节点信息
     *
     * @param targetObj 目标对象
     * @param children  子节点信息
     */
    void setChildren(T targetObj, List<T> children) {
        methodInvoke(childrenSetter, targetObj, children);
    }
}
