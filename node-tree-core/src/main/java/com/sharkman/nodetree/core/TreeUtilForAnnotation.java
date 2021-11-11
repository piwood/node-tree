package com.sharkman.nodetree.core;

import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p> Description:树工具类，利用 id，pid 生成父子形式的树结构</p>
 * <p> CreationTime: 2020/2/5 20:41
 * <br>Email: <a href="mailto:526478642@qq.com">526478642@qq.com</a></p>
 *
 * @author yanpengyu
 * @version 1.0
 * @since 1.0
 */
public final class TreeUtilForAnnotation {
    private TreeUtilForAnnotation() {
    }

    /**
     * 构建树形结构，并返回所有根节点
     * <p>传入一个 Predicate 对想来判别根节点，若节点满足此条件，则设置为根节点。</p>
     *
     * @param vos       所有节点数据
     * @param predicate 判别式，为true则为根节点
     * @return 所有根节点
     */
    public static <T> List<T> buildTreeForList(List<T> vos, @NonNull Predicate<T> predicate) {
        if (null == vos || vos.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> maybeRoots = buildTree(vos);
        // 从疑似父节点中查找真正的父节点
        List<T> roots = new ArrayList<>();
        for (T vo : maybeRoots) {
            if (predicate.test(vo)) {
                roots.add(vo);
            }
        }
        // 没找到，可能为子树，整体过滤
        if (CollectionUtils.isEmpty(roots)) {
            roots = vos.stream()
                    .filter(predicate)
                    .collect(Collectors.toList());
        }
        return roots;
    }

    /**
     * 构建树，返回第一级节点
     *
     * @param vos 原始对象
     * @param <T> 对象类型
     * @return 第一级节点
     */
    public static <T> List<T> buildTree(List<T> vos) {
        TreeNodeProxy<T> proxy = TreeNodeProxy.from(vos.get(0));
        return constructTree(vos, proxy);
    }

    /**
     * 构建树形结构，并返回根节点
     *
     * @param vos 所有节点数据
     * @param id  指定根节点的id，不可为null
     * @return 根节点
     */
    public static <T> T buildTreeOfRootId(List<T> vos, String id) {
        if (null == id) {
            throw new IllegalArgumentException("构建树失败！根节点id不能为空！");
        }
        if (null == vos || vos.isEmpty()) {
            return null;
        }
        TreeNodeProxy<T> proxy = TreeNodeProxy.from(vos.get(0));
        List<T> maybeRoots = constructTree(vos, proxy);
        for (T root : maybeRoots) {
            if (proxy.getId(root).equals(id)) {
                return root;
            }
        }
        return null;
    }

    /**
     * 构建树形结构，并返回根节点
     *
     * @param vos 所有节点数据
     * @param pid 指定根节点的父id， 可为 null
     * @return 根节点
     */
    public static <T> T buildTreeOfRootPId(List<T> vos, String pid) {
        return buildTreeOfRootPIdForList(vos, pid).get(0);
    }

    /**
     * 构建树形结构，并返回所有根节点
     *
     * @param vos 所有节点数据
     * @param pid 指定根节点的父id， 可为 null
     * @return 所有根节点
     */
    public static <T> List<T> buildTreeOfRootPIdForList(List<T> vos, String pid) {
        if (null == vos || vos.isEmpty()) {
            return Collections.emptyList();
        }
        TreeNodeProxy<T> proxy = TreeNodeProxy.from(vos.get(0));
        List<T> maybeRoots = constructTree(vos, proxy);
        List<T> result = new ArrayList<>();
        for (T root : maybeRoots) {
            if (Objects.equals(proxy.getPId(root), pid)) {
                result.add(root);
            }
        }
        return result;
    }


    /**
     * 构造树核心方法
     *
     * @param vos 树节点对象集合
     * @param <T> 树节点
     * @return 返回无父节点的节点
     */
    private static <T> List<T> constructTree(List<T> vos, TreeNodeProxy<T> proxy) {
        // 可能为根节点的
        // 创建代理工具对象
        List<T> maybeRoots = new ArrayList<>();
        Map<String, T> temp = new HashMap<>(vos.size());
        // 先把所有节点都糊上
        for (T vo : vos) {
            temp.put(proxy.getId(vo), vo);
        }
        // 构造树
        for (T vo : vos) {
            T father = temp.get(proxy.getPId(vo));
            // 没有父节点的，则为疑似父节点
            if (null == father || father == vo) {
                maybeRoots.add(vo);
                continue;
            }
            List<T> brothers = proxy.getChildren(father);
            if (null == brothers || brothers.isEmpty()) {
                brothers = new ArrayList<>();
                brothers.add(vo);
                proxy.setChildren(father, brothers);
            } else {
                brothers.add(vo);
            }
        }
        return maybeRoots;
    }


    /**
     * 根据权限反向构造树结构
     *
     * @param treeNodes 组织机构树
     * @param ids       树id
     * @param rootId    根节点id
     * @return 组织机构根层节点
     */
    public static <T> List<T> constructTreeForSpecifyNode(
            List<T> treeNodes, List<String> ids, String rootId) {
        // 构造节点映射对象
        TreeNodeProxy<T> proxy = TreeNodeProxy.from(treeNodes.get(0));
        Map<String, T> temp = constructTempMap(treeNodes, proxy);
        // 构造队列
        Queue<T> queue = initQueue(ids, temp);
        // 拼接树
        jointTree(temp, queue, proxy);
        // 获取根节点
        return getRootNode(treeNodes, rootId, temp, proxy);
    }


    // 构造节点映射对象
    private static <T> Map<String, T> constructTempMap(List<T> treeNodes, TreeNodeProxy<T> proxy) {
        Map<String, T> temp = new HashMap<>(treeNodes.size());
        for (T treeNode : treeNodes) {
            temp.put(proxy.getId(treeNode), treeNode);
        }
        return temp;
    }

    // 拼接树
    private static <T> void jointTree(Map<String, T> temp, Queue<T> queue, TreeNodeProxy<T> proxy) {
        while (!queue.isEmpty()) {
            T curNode = queue.poll();
            // 1. 把自己的父亲节点放入队列
            if (null == curNode) {
                throw new NullPointerException("获取数据异常，存在空节点");
            }
            T parent = temp.get(proxy.getPId(curNode));
            if (null == parent) {
                continue;
            }
            queue.add(parent);
            // 2. 把自己与父亲节点关联起来
            List<T> curBrothers = proxy.getChildren(parent);
            if (null == curBrothers) {
                curBrothers = new ArrayList<>();
                proxy.setChildren(parent, curBrothers);
            }
            if (notContainsNode(curBrothers, curNode, proxy)) {
                curBrothers.add(curNode);
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
            List<T> treeNodes, String rootId, Map<String, T> temp, TreeNodeProxy<T> proxy) {
        T root = temp.get(rootId);
        if (null != root) {
            return Collections.singletonList(temp.get(rootId));
        }
        return findAllRoot(treeNodes, rootId, proxy);
    }

    // 获取所有根节点
    private static <T> List<T> findAllRoot(List<T> treeNodes, String rootId, TreeNodeProxy<T> proxy) {
        List<T> roots = new ArrayList<>();
        for (T treeNode : treeNodes) {
            if (null != proxy.getPId(treeNode) && proxy.getPId(treeNode).equals(rootId) &&
                    null != proxy.getChildren(treeNode) && !proxy.getChildren(treeNode).isEmpty()) {
                roots.add(treeNode);
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
    private static <T> boolean notContainsNode(List<T> origin, T treeNode, TreeNodeProxy<T> proxy) {
        for (T node : origin) {
            if (proxy.getId(node).equals(proxy.getId(treeNode))) {
                return false;
            }
        }
        return true;
    }
}
