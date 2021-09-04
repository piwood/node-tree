package com.sharkman.nodetree.aspect;

import java.util.List;

/**
 * <p> Description:创建树接口</p>
 * <p> CreationTime: 2021/9/4 13:41
 *
 * @author piwood
 * @version 1.0
 * @since 1.1.4
 */
interface TreeCreator {
    /**
     * 构建树形结构，并返回根节点
     *
     * @param vos 所有节点数据
     * @param id  指定根节点的id，不可为null
     * @return 根节点
     */
    Object buildTreeOfRootId(List<Object> vos, String id);

    /**
     * 构建树形结构，并返回所有根节点
     *
     * @param vos 所有节点数据
     * @param pid 指定根节点的父id， 可为 null
     * @return 所有根节点
     */
    List<Object> buildTreeOfRootPIdForList(List<Object> vos, String pid);


    /**
     * 构建树，返回第一级节点
     *
     * @param vos 原始对象
     * @return 第一级节点
     */
    List<Object> buildTree(List<Object> vos);
}
