package com.sharkman.nodetree.core;

import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.sharkman.nodetree.core.TreeCoreConstructor.constructTreeForTemp;

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
    private TreeUtil() throws IllegalAccessException {
        throw new IllegalAccessException("非法访问构造函数");
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
    public static <T> T buildTree(List<T> vos, @NonNull Predicate<T> predicate) {
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
     * @param nodes     所有节点数据
     * @param predicate 判别式，为true则为根节点
     * @return 所有根节点
     */
    static <T> List<T> buildTreeForListProxy(
            List<TreeNodeProxy<T>> nodes, @NonNull Predicate<TreeNodeProxy<T>> predicate) {
        List<TreeNodeProxy<T>> maybeRoots = TreeCoreConstructor.constructTree(nodes);
        // 从疑似父节点中查找真正的父节点
        List<T> roots = new ArrayList<>();
        for (TreeNodeProxy<T> vo : maybeRoots) {
            if (predicate.test(vo)) {
                roots.add(vo.getOrigin());
            }
        }
        // 没找到，可能为子树，整体过滤
        if (CollectionUtils.isEmpty(roots)) {
            roots = nodes.stream()
                    .filter(predicate)
                    .map(TreeNodeProxy::getOrigin)
                    .collect(Collectors.toList());
        }
        return roots;
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
        return buildTreeForListProxy(TreeNodeProxy.ofList(vos), n -> predicate.test(n.getOrigin()));
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
        return constructTreeForTemp(TreeNodeProxy.ofList(vos)).get(id).getOrigin();
    }

    /**
     * 构建树形结构，并返回根节点
     * 使用{@link Objects#equals(Object, Object)} 判断是否相等
     *
     * @param nodes 所有节点数据
     * @param pid   指定根节点的父id， 可为 null;
     * @return 根节点
     */
    public static <T> T buildTreeOfRootPId(List<T> nodes, String pid) {
        return Optional.ofNullable(buildTreeOfRootPIdForList(nodes, pid))
                .map(l -> l.get(0)).orElse(null);
    }

    /**
     * 构建树形结构，并返回所有根节点
     *
     * @param nodes 所有节点数据
     * @param pid   指定根节点的父id， 可为 null
     * @return 所有根节点
     */
    public static <T> List<T> buildTreeOfRootPIdForList(List<T> nodes, String pid) {
        if (null == nodes || nodes.isEmpty()) {
            return null;
        }
        TreeNodeWrapper<T> wrapper = TreeNodeProxy.createNodeWrapper(nodes.get(0));
        List<T> result =
                buildTreeForListProxy(TreeNodeProxy.ofList(nodes, wrapper), n -> Objects.equals(n.getPId(), pid));
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    /**
     * 根据权限反向构造树结构
     *
     * @param treeNodes 组织机构树
     * @param ids       树id
     * @param rootId    子树根节点id
     * @return 组织机构根层节点
     */
    public static <T> List<T> buildTreeForSpecifyNode(
            List<T> treeNodes, List<String> ids, String rootId) {
        return TreeCoreConstructor.constructTreeForSpecifyNode(TreeNodeProxy.ofList(treeNodes), ids, rootId);
    }

    /**
     * 基本构造树,返回可能的节点
     *
     * @param vos 树节点对象集合
     * @param <T> 树节点
     * @return 返回无父节点的节点
     */
    public static <T> List<T> buildTree(List<T> vos) {
        List<TreeNodeProxy<T>> result = TreeCoreConstructor.constructTree(TreeNodeProxy.ofList(vos));
        if (result.isEmpty()) {
            return Collections.emptyList();
        }
        return result.stream()
                .map(TreeNodeProxy::getOrigin)
                .collect(Collectors.toList());
    }
}
