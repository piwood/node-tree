package com.sharkman.nodetree.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReflectUtilTest {

    @Test
    void isPrimitive() {
        Demo demo = new Demo();
        assertFalse(ReflectUtil.isPrimitiveOrWrapper(demo.getClass()));
        assertTrue(ReflectUtil.isPrimitiveOrWrapper(Integer.class));
        assertTrue(ReflectUtil.isPrimitiveOrWrapper(Double.class));
        assertTrue(ReflectUtil.isPrimitiveOrWrapper(Character.class));
        assertTrue(ReflectUtil.isPrimitiveOrWrapper(Boolean.class));
        assertTrue(ReflectUtil.isPrimitiveOrWrapper(Byte.class));
        assertTrue(ReflectUtil.isPrimitiveOrWrapper(Short.class));
        assertTrue(ReflectUtil.isPrimitiveOrWrapper(Long.class));
        assertTrue(ReflectUtil.isPrimitiveOrWrapper(Float.class));
    }

    private static class Demo {
        private int id;
        private String name;
    }

    // 定义一个测试用的注解
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestAnnotation {
    }

    // 测试类，用于测试findColumnByAnnotation方法
    public static class TestEntity {
        @TestAnnotation
        private String annotatedField;
        public String nonAnnotatedField;
    }

    // 定义一个子类，实现 TestEntity 类，增加 name 属性
    public static class TestEntityWithName extends TestEntity {
        private String name;
    }

    @Test
    void testFindColumnByAnnotationFound() {
        // 测试找到带有注解的字段
        Field field = ReflectUtil.findColumnByAnnotation(TestEntity.class, TestAnnotation.class);
        Assertions.assertNotNull(field, "Field should not be null.");
        Assertions.assertEquals("annotatedField", field.getName(), "Field name should match.");
        Assertions.assertTrue(field.isAnnotationPresent(TestAnnotation.class), "Field should have the annotation.");

        field = ReflectUtil.findColumnByAnnotation(TestEntityWithName.class, TestAnnotation.class);
        Assertions.assertNotNull(field, "Field should not be null.");
        Assertions.assertEquals("annotatedField", field.getName(), "Field name should match.");
        Assertions.assertTrue(field.isAnnotationPresent(TestAnnotation.class), "Field should have the annotation.");

    }

    @Test
    void testFindColumnByAnnotationNotFound() {
        // 测试未找到带有注解的字段
        Field field = ReflectUtil.findColumnByAnnotation(TestEntity.class, java.lang.Deprecated.class);
        Assertions.assertNull(field, "Field should be null because the annotation is not present.");
    }
}