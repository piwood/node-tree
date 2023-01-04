package com.sharkman.nodetree.core;

import java.util.*;

/**
 * <p> Description:核心构造器</p>
 * <p> CreationTime: 2022/11/13 1:18 PM
 *
 * @author Piwood
 * @version 1.0
 * @since 1.0
 */
final class TreeCoreConstructor {
    private TreeCoreConstructor() {
    }

    // 构造节点映射对象
    private static <T> Map<String, TreeNodeProxy<T>> constructTempMap(List<TreeNodeProxy<T>> treeNodes) {
        Map<String, TreeNodeProxy<T>> temp = new HashMap<>(treeNodes.size());
        for (TreeNodeProxy<T> treeNode : treeNodes) {
            temp.put(treeNode.getId(), treeNode);
        }
        return temp;
    }

    // 拼接树
    private static <T> void jointTree(Map<String, TreeNodeProxy<T>> temp, Queue<TreeNodeProxy<T>> queue) {
        while (!queue.isEmpty()) {
            TreeNodeProxy<T> curNode = queue.poll();
            // 1. 把自己的父亲节点放入队列
            if (null == curNode) {
                throw new NullPointerException("获取数据异常，存在空节点");
            }
            TreeNodeProxy<T> parent = temp.get(curNode.getPId());
            if (null == parent) {
                continue;
            }
            queue.add(parent);
            // 2. 把自己与父亲节点关联起来
            List<TreeNodeProxy<T>> curBrothers = parent.getChildren();
            if (notContainsNode(curBrothers, curNode)) {
                parent.addChild(curNode);
            }
        }
    }

    // 构造队列
    private static <T> Queue<T> initQueue(List<String> ids, Map<String, T> temp) {
        Queue<T> queue = new ArrayDeque<>();
        for (String id : ids) {
            T curUser = temp.get(id);
            if (null == curUser) {
                continue;
            }
            queue.add(curUser);
        }
        return queue;
    }

    // 获取根节点
    private static <T> List<T> getRootNode(
            List<TreeNodeProxy<T>> treeNodes, String rootId, Map<String, TreeNodeProxy<T>> temp) {
        TreeNodeProxy<T> root = temp.get(rootId);
        if (null != root) {
            return Collections.singletonList(temp.get(rootId).getOrigin());
        }
        return findAllRoot(treeNodes, rootId);
    }

    // 获取所有根节点
    private static <T> List<T> findAllRoot(List<TreeNodeProxy<T>> treeNodes, String rootId) {
        List<T> roots = new ArrayList<>();
        for (TreeNodeProxy<T> treeNode : treeNodes) {
            if (null != treeNode.getPId() && treeNode.getPId().equals(rootId) &&
                    null != treeNode.getChildren() && !treeNode.getChildren().isEmpty()) {
                roots.add(treeNode.getOrigin());
            }
        }
        return roots;
    }

    /**
     * 包含
     *
     * @param origin   原始 list
     * @param treeNode 树节点
     * @return 是否包含
     */
    private static <T> boolean notContainsNode(List<TreeNodeProxy<T>> origin, TreeNodeProxy<T> treeNode) {
        for (TreeNodeProxy<T> node : origin) {
            if (node.getId().equals(treeNode.getId())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 构造树核心方法
     *
     * @param vos 树节点对象集合
     * @param <T> 树节点
     * @return 返回无父节点的节点
     */
    static <T> Map<String, TreeNodeProxy<T>> constructTreeForTemp(List<TreeNodeProxy<T>> vos) {
        if (null == vos || vos.isEmpty()) {
            return Collections.emptyMap();
        }
        // 可能为根节点的
        Map<String, TreeNodeProxy<T>> temp = new HashMap<>(vos.size());
        // 先把所有节点都糊上
        for (TreeNodeProxy<T> vo : vos) {
            temp.put(vo.getId(), vo);
        }
        // 构造树
        for (TreeNodeProxy<T> vo : vos) {
            TreeNodeProxy<T> father = temp.get(vo.getPId());
            // 没有父节点的，则为疑似父节点
            if (null == father || father == vo) {
                continue;
            }
            father.addChild(vo);
        }
        return temp;
    }

    /**
     * 根据权限反向构造树结构
     *
     * @param treeNodes 组织机构树
     * @param ids       树id
     * @param rootId    子树根节点id
     * @return 组织机构根层节点
     */
    static <T> List<T> constructTreeForSpecifyNode(
            List<TreeNodeProxy<T>> treeNodes, List<String> ids, String rootId) {
        // 构造节点映射对象
        Map<String, TreeNodeProxy<T>> temp = constructTempMap(treeNodes);
        // 构造队列
        Queue<TreeNodeProxy<T>> queue = initQueue(ids, temp);
        // 拼接树
        jointTree(temp, queue);
        // 获取根节点
        return getRootNode(treeNodes, rootId, temp);
    }

    /**
     * 基本构造树
     *
     * @param vos 树节点对象集合
     * @param <T> 树节点
     * @return 返回无父节点的节点
     */
    static <T> List<TreeNodeProxy<T>> constructTree(List<TreeNodeProxy<T>> vos) {
        if (null == vos || vos.isEmpty()) {
            return Collections.emptyList();
        }
        // 可能为根节点的
        List<TreeNodeProxy<T>> maybeRoots = new ArrayList<>();
        Map<String, TreeNodeProxy<T>> temp = constructTreeForTemp(vos);
        // 构造树
        for (TreeNodeProxy<T> vo : vos) {
            TreeNodeProxy<T> father = temp.get(vo.getPId());
            // 没有父节点的，则为疑似父节点
            if (null == father || father == vo) {
                maybeRoots.add(vo);
            }
        }
        return maybeRoots;
    }

    /**
     * 计算树上有多少个节点
     *
     * @param trees 数结构
     * @return 节点数量
     */
    static <T> int countNodes(List<T> trees, TreeNodeWrapper<T> wrapper) {
        if (null == trees || trees.isEmpty()) {
            return 0;
        }
        Queue<T> queue = new LinkedList<>(trees);
        int count = 0;
        while (!queue.isEmpty()) {
            T current = queue.poll();
            count++;
            if (null != current) {
                List<T> children = wrapper.getChildren(current);
                if (null != children) {
                    queue.addAll(children);
                }
            }
        }
        return count;
    }
}
