package com.sharkman.nodetree.core;

import java.util.List;

/**
 * <p> Description:树节点反射包装</p>
 * <p> CreationTime: 2021/5/6 18:02
 * <br>Email: <a href="mailto:526478642@qq.com">526478642@qq.com</a></p>
 *
 * @author yanpengyu
 * @version 1.0
 * @since 1.0
 */
final class TreeNodeWrapperInterface<T extends Treeable> implements TreeNodeWrapper<T> {

    /**
     * 获取树节点 id
     *
     * @param targetObj 目标对象
     * @return 树节点 id
     */
    @Override
    public String getId(T targetObj) {
        return targetObj.getId();
    }

    /**
     * 获取树节点父节点 id
     *
     * @param targetObj 目标对象
     * @return 父节点 id
     */
    @Override
    public String getPId(T targetObj) {
        return targetObj.getPId();
    }

    /**
     * 获取子节点
     *
     * @param targetObj 目标对象
     * @return 孩子节点
     */
    @Override
    public List<T> getChildren(T targetObj) {
        return (List<T>) targetObj.getChildren();
    }

    /**
     * 设置子节点信息
     *
     * @param targetObj 目标对象
     * @param children  子节点信息
     */
    @Override
    public void setChildren(List<T> children, T targetObj) {
        targetObj.setChildren((List<Treeable>) children);
    }
}
