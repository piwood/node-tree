package com.sharkman.nodetree.core;

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
     * 获取孩子节点
     *
     * @return 孩子节点
     */
    public List<T> getChildren() {
        return wrapper.getChildren(origin);
    }

    /**
     * 设置孩子节点
     *
     * @param children 孩子节点
     */
    public void setChildren(List<T> children) {
        wrapper.setChildren(children, origin);
    }


    T getOrigin() {
        return origin;
    }

    TreeNodeWrapper<T> getWrapper() {
        return wrapper;
    }
}
