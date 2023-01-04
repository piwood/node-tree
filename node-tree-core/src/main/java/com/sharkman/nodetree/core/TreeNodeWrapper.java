package com.sharkman.nodetree.core;

import java.util.List;

/**
 * <p> Description:树节点包装器，获取原对象属性策略</p>
 * <p> CreationTime: 2022/11/13 11:20 AM
 *
 * @author Piwood
 * @version 1.0
 * @since 1.0
 */
public interface TreeNodeWrapper<T> {
    /**
     * 获取树节点 id
     *
     * @param targetObj 目标对象
     * @return 树节点 id
     */
    String getId(T targetObj);

    /**
     * 获取树节点父节点 id
     *
     * @param targetObj 目标对象
     * @return 父节点 id
     */
    String getPId(T targetObj);

    /**
     * 获取子节点
     *
     * @param targetObj 目标对象
     * @return 孩子节点
     */
    List<T> getChildren(T targetObj);

    /**
     * 设置子节点信息
     *
     * @param children  子节点信息
     * @param targetObj 目标对象
     */
    void setChildren(List<T> children, T targetObj);
}
