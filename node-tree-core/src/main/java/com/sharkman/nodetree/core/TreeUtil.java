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
public final class TreeUtil {
    private TreeUtil() {
    }

    /**
     * 构建树形结构，并返回根节点
     * <p>通过 id 和 pid 构建树，若 id 有重复可能会造成相同id对象只有一个有子节点的情况，请自行去重</p>
     * <p>传入一个 Predicate 对想来判别跟节点，若节点满足此条件，则设置为根节点。如果多个节点满足，则只取一个。</p>
     *
     * @param vos       所有节点数据
     * @param predicate 判别式，为true则为根节点
     * @return 根节点
     */
    public static <T extends Treeable> T buildTree(List<T> vos, @NonNull Predicate<T> predicate) {
        List<T> result = buildTreeForList(vos, predicate);
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    /**
     * 构建树形结构，并返回所有根节点
     * <p>传入一个 Predicate 对想来判别根节点，若节点满足此条件，则设置为根节点。</p>
     *
     * @param vos       所有节点数据
     * @param predicate 判别式，为true则为根节点
     * @return 所有根节点
     */
    public static <T extends Treeable> List<T> buildTreeForList(List<T> vos, @NonNull Predicate<T> predicate) {
        List<T> maybeRoots = constructTree(vos);
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
     * 构建树形结构，并返回根节点
     *
     * @param vos 所有节点数据
     * @param id  指定根节点的id，不可为null
     * @return 根节点
     */
    public static <T extends Treeable> T buildTreeOfRootId(List<T> vos, String id) {
        if (null == id) {
            throw new IllegalArgumentException("构建树失败！根节点id不能为空！");
        }
        return constructTreeForTemp(vos).get(id);
    }

    /**
     * 构建树形结构，并返回根节点
     *
     * @param vos 所有节点数据
     * @param pid 指定根节点的父id， 可为 null
     * @return 根节点
     */
    public static <T extends Treeable> T buildTreeOfRootPId(List<T> vos, String pid) {
        return buildTree(vos, vo -> Objects.equals(vo.getPId(), pid));
    }

    /**
     * 构建树形结构，并返回所有根节点
     *
     * @param vos 所有节点数据
     * @param pid 指定根节点的父id， 可为 null
     * @return 所有根节点
     */
    public static <T extends Treeable> List<T> buildTreeOfRootPIdForList(List<T> vos, String pid) {
        return buildTreeForList(vos, vo -> Objects.equals(vo.getPId(), pid));
    }

    /**
     * 计算树上有多少个节点
     * @param trees 数结构
     * @return 节点数量
     */
    public static <T extends Treeable> int countNodes(List<T> trees) {
        if (null == trees || trees.isEmpty()) {
            return 0;
        }
        Queue<Treeable> queue = new LinkedList<>(trees);
        int count = 0;
        while (!queue.isEmpty()) {
            Treeable current = queue.poll();
            count++;

            List<Treeable> children;
            if (null == current || null == current.getChildren()) {
                children = Collections.emptyList();
            } else {
                children = current.getChildren();
            }
            if (!children.isEmpty()) {
                queue.addAll(children);
            }
        }
        return count;
    }


    /**
     * 基本构造树
     *
     * @param vos 树节点对象集合
     * @param <T> 树节点
     * @return 返回无父节点的节点
     */
    public static <T extends Treeable> List<T> constructTree(List<T> vos) {
        if (null == vos || vos.isEmpty()) {
            return Collections.emptyList();
        }
        // 可能为根节点的
        List<T> maybeRoots = new ArrayList<>();
        Map<String, T> temp = constructTreeForTemp(vos);
        // 构造树
        for (T vo : vos) {
            T father = temp.get(vo.getPId());
            // 没有父节点的，则为疑似父节点
            if (null == father || father == vo) {
                maybeRoots.add(vo);
            }
        }
        return maybeRoots;
    }

    /**
     * 构造树核心方法
     *
     * @param vos 树节点对象集合
     * @param <T> 树节点
     * @return 返回无父节点的节点
     */
    private static <T extends Treeable> Map<String, T> constructTreeForTemp(List<T> vos) {
        if (null == vos || vos.isEmpty()) {
            return Collections.emptyMap();
        }
        // 可能为根节点的
        Map<String, T> temp = new HashMap<>(vos.size());
        // 先把所有节点都糊上
        for (T vo : vos) {
            temp.put(vo.getId(), vo);
        }
        // 构造树
        for (T vo : vos) {
            T father = temp.get(vo.getPId());
            // 没有父节点的，则为疑似父节点
            if (null == father || father == vo) {
                continue;
            }
            List<Treeable> brothers = father.getChildren();
            if (null == brothers) {
                brothers = new ArrayList<>();
                father.setChildren(brothers);
            }
            brothers.add(vo);
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
    public static <T extends Treeable> List<T> constructTreeForSpecifyNode(
            List<T> treeNodes, List<String> ids, String rootId) {
        // 构造节点映射对象
        Map<String, T> temp = constructTempMap(treeNodes);
        // 构造队列
        Queue<T> queue = initQueue(ids, temp);
        // 拼接树
        jointTree(temp, queue);
        // 获取根节点
        return getRootNode(treeNodes, rootId, temp);
    }


    // 构造节点映射对象
    private static <T extends Treeable> Map<String, T> constructTempMap(List<T> treeNodes) {
        Map<String, T> temp = new HashMap<>(treeNodes.size());
        for (T treeNode : treeNodes) {
            temp.put(treeNode.getId(), treeNode);
        }
        return temp;
    }

    // 拼接树
    private static <T extends Treeable> void jointTree(Map<String, T> temp, Queue<T> queue) {
        while (!queue.isEmpty()) {
            T curNode = queue.poll();
            // 1. 把自己的父亲节点放入队列
            if (null == curNode) {
                throw new NullPointerException("获取数据异常，存在空节点");
            }
            T parent = temp.get(curNode.getPId());
            if (null == parent) {
                continue;
            }
            queue.add(parent);
            // 2. 把自己与父亲节点关联起来
            List<Treeable> curBrothers = parent.getChildren();
            if (null == curBrothers) {
                curBrothers = new ArrayList<>();
                parent.setChildren(curBrothers);
            }
            if (notContainsNode(curBrothers, curNode)) {
                curBrothers.add(curNode);
            }
        }
    }

    // 构造队列
    private static <T extends Treeable> Queue<T> initQueue(List<String> ids, Map<String, T> temp) {
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
    private static <T extends Treeable> List<T> getRootNode(List<T> treeNodes, String rootId, Map<String, T> temp) {
        T root = temp.get(rootId);
        if (null != root) {
            return Collections.singletonList(temp.get(rootId));
        }
        return findAllRoot(treeNodes, rootId);
    }

    // 获取所有根节点
    private static <T extends Treeable> List<T> findAllRoot(List<T> treeNodes, String rootId) {
        List<T> roots = new ArrayList<>();
        for (T treeNode : treeNodes) {
            if (null != treeNode.getPId() && treeNode.getPId().equals(rootId) &&
                    null != treeNode.getChildren() && !treeNode.getChildren().isEmpty()) {
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
    private static boolean notContainsNode(List<Treeable> origin, Treeable treeNode) {
        for (Treeable node : origin) {
            if (node.getId().equals(treeNode.getId())) {
                return false;
            }
        }
        return true;
    }
}
