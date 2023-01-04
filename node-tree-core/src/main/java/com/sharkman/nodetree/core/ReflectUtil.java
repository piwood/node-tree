package com.sharkman.nodetree.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p> Description:反射工具类</p>
 * <p> CreationTime: 2021/9/4 17:59
 *
 * @author piwood
 * @version 1.0
 * @since 1.1.4
 */
public final class ReflectUtil {

    private static final String METHOD_NOT_FOUND_MESSAGE = "没有找到 %s 注解对应字段 %s 的getter或setter ！";
    private static final String FIELD_NOT_FOUND_MESSAGE = "没有找到 %s 注解的属性 ！";
    private static final String INVOKE_ACCESS_ERROR_MESSAGE =
            "调用类%s的%s方法失败！请检查方法及类的访问符是否为public！错误信息为%s";
    private static final String INVOKE_OTHER_ERROR_MESSAGE =
            "调用类%s的%s方法失败！请检查方法参数！错误信息为%s";

    /**
     * 字母数量
     */
    private static final int ALPHABET_NUM = 32;

    private static final String STR_GET = "get";
    private static final String STR_SET = "set";


    private ReflectUtil() throws IllegalAccessException {
        throw new IllegalAccessException("非法访问构造函数");
    }

    /**
     * 获取所有属性上指定注解
     *
     * @param fields 所有属性
     * @param ann    指定注解
     * @return 字段，若没找到，返回null
     */
    public static Field findColumnByAnnotation(Field[] fields, Class<? extends Annotation> ann) {
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
     * 反射 get 方法
     *
     * @param clazz     类
     * @param fieldName 字段
     * @return get方法
     * @throws NoSuchMethodException 未找到该方法时抛出
     */
    public static Method refGetMethod(Class<?> clazz, String fieldName) throws NoSuchMethodException {
        return clazz.getMethod(STR_GET + ReflectUtil.upperFirstCase(fieldName));
    }

    /**
     * 反射 Set 方法
     *
     * @param clazz     类
     * @param fieldName 字段
     * @return Set方法
     * @throws NoSuchMethodException 未找到该方法时抛出
     */
    static Method refSetMethod(Class<?> clazz, String fieldName, Class<?>... params)
            throws NoSuchMethodException {
        return clazz.getMethod(STR_SET + ReflectUtil.upperFirstCase(fieldName), params);
    }

    /**
     * 方法反射
     *
     * @param method    方法
     * @param targetObj 调用方法的类
     * @param params    参数
     * @return 返回值
     */
    public static Object methodInvoke(Method method, Object targetObj, Object... params) {
        try {
            return method.invoke(targetObj, params);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(
                    String.format(
                            INVOKE_ACCESS_ERROR_MESSAGE,
                            targetObj.getClass().getName(),
                            method.getName(),
                            e.getMessage()), e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException(
                    String.format(
                            INVOKE_OTHER_ERROR_MESSAGE,
                            targetObj.getClass().getName(),
                            method.getName(),
                            e.getMessage()), e);
        }

    }

    /**
     * 未找到指定注解的属性异常
     *
     * @param annotationName 注解名称
     * @return 异常信息
     */
    static String filedNotFoundMessageByAnnotation(String annotationName) {
        return String.format(FIELD_NOT_FOUND_MESSAGE, annotationName);
    }

    /**
     * 未找到指定注解的方法异常
     *
     * @param annotationName 注解名称
     * @param field          属性
     * @return 异常信息
     */
    public static String methodNotFoundByAnnotationMessage(String annotationName, Field field) {
        return String.format(METHOD_NOT_FOUND_MESSAGE, annotationName, field.getName());
    }

    /**
     * 判断是否是基本类型或者基本类型包装类
     *
     * @param clazz 类
     * @return true 是，false 否
     */
    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return true;
        }
        return Number.class.isAssignableFrom(clazz) ||
                clazz.isAssignableFrom(Boolean.class) || clazz.isAssignableFrom(Character.class);
    }
}
