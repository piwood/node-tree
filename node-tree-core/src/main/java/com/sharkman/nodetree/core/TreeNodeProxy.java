package com.sharkman.nodetree.core;

import java.util.List;

/**
 * <p> Description:树节点反射代理</p>
 * <p> CreationTime: 2021/5/6 18:02
 * <br>Email: <a href="mailto:526478642@qq.com">526478642@qq.com</a></p>
 *
 * @author yanpengyu
 * @version 1.0
 * @since 1.0
 */
public class TreeNodeProxy<T> {
    /**
     * 获取树节点 id
     *
     * @param targetObj 被
     * @return 树节点 id
     */
    String getId(T targetObj) {
        return null;
    }

    /**
     * 获取树节点父节点 id
     *
     * @return 父节点 id
     */
    String getPId(T targetObj) {
        return null;
    }

    /**
     * 获取子节点
     *
     * @return 孩子节点
     */
    List<T> getChildren(T targetObj) {
        return null;
    }

    /**
     * 设置子节点信息
     *
     * @param children 子节点信息
     */
    void setChildren(T targetObj, List<T> children) {

    }
}
