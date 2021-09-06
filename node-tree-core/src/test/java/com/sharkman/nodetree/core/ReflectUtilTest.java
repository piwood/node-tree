package com.sharkman.nodetree.core;

import org.junit.jupiter.api.Test;

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
}