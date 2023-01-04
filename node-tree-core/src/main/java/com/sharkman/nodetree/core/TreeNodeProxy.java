package com.sharkman.nodetree.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p> Description:树节点代理</p>
 * <p> CreationTime: 2022/11/11 12:39 PM
 *
 * @author Piwood
 * @version 1.0
 * @since 1.0
 */
public class TreeNodeProxy<T> {
    private final T origin;
    private final TreeNodeWrapper<T> wrapper;
    private List<TreeNodeProxy<T>> children;

    /**
     * 构造函数
     *
     * @param origin  代理对象
     * @param wrapper 包装器
     */
    public TreeNodeProxy(T origin, TreeNodeWrapper<T> wrapper) {
        this.wrapper = wrapper;
        this.origin = origin;
    }

    /**
     * 构造一个代理节点
     *
     * @param t       原对象
     * @param wrapper 节点包装器，用于读取属性注解
     * @param <T>     节点类型
     * @return 代理节点
     */
    public static <T> TreeNodeProxy<T> of(T t, TreeNodeWrapper<T> wrapper) {
        if (null == wrapper) {
            throw new IllegalArgumentException("包装器不能为空");
        }
        return new TreeNodeProxy<>(t, wrapper);
    }

    /**
     * 构造一个代理节点
     *
     * @param t       原对象
     * @param wrapper 节点包装器，用于读取属性注解
     * @param <T>     节点类型
     * @return 代理节点
     */
    public static <T> List<TreeNodeProxy<T>> ofList(List<T> t, TreeNodeWrapper<T> wrapper) {
        if (null == t || t.isEmpty()) {
            return Collections.emptyList();
        }
        return t.stream()
                .map(node -> TreeNodeProxy.of(node, wrapper))
                .collect(Collectors.toList());
    }

    /**
     * 构造一个代理节点
     *
     * @param list 原对象
     * @param <T>  节点类型
     * @return 代理节点
     */
    public static <T> List<TreeNodeProxy<T>> ofList(List<T> list) {
        if (null == list || list.isEmpty()) {
            return Collections.emptyList();
        }
        T t = list.get(0);
        return TreeNodeProxy.ofList(list, createNodeWrapper(t));
    }

    /**
     * 创建包装器
     *
     * @param t   节点
     * @param <T> 节点对象
     * @return 包装器
     */
    public static <T> TreeNodeWrapper<T> createNodeWrapper(T t) {
        TreeNodeWrapper<T> wrapper;
        if (t instanceof Treeable) {
            wrapper = new TreeNodeWrapperInterface<>();
        } else {
            wrapper = TreeNodeWrapperAnnotation.from(t);
        }
        return wrapper;
    }

    /**
     * 取 id
     *
     * @return id
     */
    public String getId() {
        return wrapper.getId(origin);
    }

    /**
     * 取 父id
     *
     * @return pid
     */
    public String getPId() {
        return wrapper.getPId(origin);
    }

    /**
     * 添加子节点
     *
     * @param child 子节点
     */
    public void addChild(TreeNodeProxy<T> child) {
        List<T> origins = wrapper.getChildren(origin);
        if (null == this.children) {
            this.children = new ArrayList<>();
            if (null == origins) {
                origins = new ArrayList<>();
                wrapper.setChildren(origins, origin);
            }
        }
        this.children.add(child);
        origins.add(child.getOrigin());
    }

    /**
     * 获取孩子节点
     *
     * @return 孩子节点
     */
    public List<TreeNodeProxy<T>> getChildren() {
        return this.children;
    }

    /**
     * 设置孩子节点
     *
     * @param children 孩子节点
     */
    public void setChildren(List<TreeNodeProxy<T>> children) {
        this.children = children;
    }

    /**
     * 返回原始对象
     *
     * @return 原始对象
     */
    T getOrigin() {
        return origin;
    }

    /**
     * 获取包装类
     *
     * @return 包装类
     */
    TreeNodeWrapper<T> getWrapper() {
        return wrapper;
    }
}
