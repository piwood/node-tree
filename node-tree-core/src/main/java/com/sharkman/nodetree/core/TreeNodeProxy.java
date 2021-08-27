package com.sharkman.nodetree.core;

import com.sharkman.nodetree.annotation.NodeChildren;
import com.sharkman.nodetree.annotation.NodeID;
import com.sharkman.nodetree.annotation.NodePID;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    private static final String STR_GET = "get";
    private static final String STR_SET = "set";
    private static final String INVOKE_ERROR_MESSAGE =
            "调用类%s的%s方法失败！请检查方法及类的访问符是否为public！错误信息为%s";
    private static final String METHOD_NOT_FOUND_MESSAGE = "没有找到 %s 注解对应字段 %s 的getter或setter ！";
    private static final String FIELD_NOT_FOUND_MESSAGE = "没有找到 %s 注解的属性 ！";
    private Method nodeIdGetter;
    private Method pidGetter;
    private Method childrenGetter;
    private Method childrenSetter;

    /**
     * 字母数量
     */
    private static final int ALPHABET_NUM = 32;

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

        Field field = findColumn(fields, NodeID.class);
        if (null == field) {
            throw new IllegalArgumentException(String.format(FIELD_NOT_FOUND_MESSAGE, NodeID.class.getName()));
        }
        Method nodeIdGetter;
        try {
            nodeIdGetter = clazz.getMethod(STR_GET + upperFirstCase(field.getName()));
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    String.format(METHOD_NOT_FOUND_MESSAGE, NodeID.class.getName(), field.getName()), e);
        }

        field = findColumn(fields, NodePID.class);
        if (null == field) {
            throw new IllegalArgumentException(String.format(FIELD_NOT_FOUND_MESSAGE, NodePID.class.getName()));
        }
        Method pidGetter;
        try {
            pidGetter = clazz.getMethod(STR_GET + upperFirstCase(field.getName()));
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    String.format(METHOD_NOT_FOUND_MESSAGE, NodePID.class.getName(), field.getName()), e);
        }

        field = findColumn(fields, NodeChildren.class);
        if (null == field) {
            throw new IllegalArgumentException(String.format(FIELD_NOT_FOUND_MESSAGE, NodeChildren.class.getName()));
        }
        Method childrenGetter;
        Method childrenSetter;
        try {
            String camelName = upperFirstCase(field.getName());
            childrenGetter = clazz.getDeclaredMethod(STR_GET + camelName);
            childrenSetter = clazz.getDeclaredMethod(STR_SET + camelName, List.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    String.format(METHOD_NOT_FOUND_MESSAGE, NodeChildren.class.getName(), field.getName()), e);
        }
        return new TreeNodeProxy<>(nodeIdGetter, pidGetter, childrenGetter, childrenSetter);
    }

    private static Field findColumn(Field[] fields, Class<? extends Annotation> ann) {
        for (Field field : fields) {
            if (field.isAnnotationPresent(ann)) {
                return field;
            }
        }
        return null;
    }

    /**
     * 首字母大写
     *
     * @param str 字符
     * @return 转换后的
     */
    private static String upperFirstCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - ALPHABET_NUM);
        }
        return new String(ch);
    }

    /**
     * 获取树节点 id
     *
     * @param targetObj 目标对象
     * @return 树节点 id
     */
    String getId(T targetObj) {
        try {
            return Optional.ofNullable(nodeIdGetter.invoke(targetObj))
                    .map(Object::toString)
                    .orElse(null);

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(
                    String.format(
                            INVOKE_ERROR_MESSAGE,
                            targetObj.getClass().getName(),
                            nodeIdGetter.getName(),
                            e.getMessage()), e);
        }
    }

    /**
     * 获取树节点父节点 id
     *
     * @param targetObj 目标对象
     * @return 父节点 id
     */
    String getPId(T targetObj) {
        try {
            return Optional.ofNullable(pidGetter.invoke(targetObj))
                    .map(Object::toString)
                    .orElse(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(
                    String.format(
                            INVOKE_ERROR_MESSAGE,
                            targetObj.getClass().getName(),
                            pidGetter.getName(), e.getMessage()), e);
        }
    }

    /**
     * 获取子节点
     *
     * @param targetObj 目标对象
     * @return 孩子节点
     */
    List<T> getChildren(T targetObj) {
        try {
            Object children = childrenGetter.invoke(targetObj);
            if (null == children) {
                return Collections.emptyList();
            }
            if (!(children instanceof List)) {
                throw new IllegalStateException("children 为非List!转化失败, 获取 children 信息失败");
            }
            @SuppressWarnings("unchecked")
            List<T> result = (List) children;
            return result;

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(
                    String.format(
                            INVOKE_ERROR_MESSAGE,
                            targetObj.getClass().getName(),
                            childrenGetter.getName(), e.getMessage()), e);
        }
    }

    /**
     * 设置子节点信息
     *
     * @param targetObj 目标对象
     * @param children  子节点信息
     */
    void setChildren(T targetObj, List<T> children) {
        try {
            childrenSetter.invoke(targetObj, children);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(
                    String.format(
                            INVOKE_ERROR_MESSAGE,
                            targetObj.getClass().getName(),
                            childrenSetter.getName(),
                            e.getMessage()), e);
        }
    }
}
