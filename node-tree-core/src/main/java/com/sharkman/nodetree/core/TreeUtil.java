package com.sharkman.nodetree.core;

import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
        return constructTreeForTemp(ofListForInterface(vos)).get(id).getOrigin();
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
        return TreeCoreConstructor.countNodes(ofListForInterface(trees));
    }

    /**
     * 根据权限反向构造树结构
     *
     * @param treeNodes 组织机构树
     * @param ids       树id
     * @param rootId    子树根节点id
     * @return 组织机构根层节点
     */
    public static <T extends Treeable> List<T> buildTreeForSpecifyNode(
            List<T> treeNodes, List<String> ids, String rootId) {
        return TreeCoreConstructor.constructTreeForSpecifyNode(ofListForInterface(treeNodes), ids, rootId);
    }

    /**
     * 基本构造树,返回可能的节点
     *
     * @param vos 树节点对象集合
     * @param <T> 树节点
     * @return 返回无父节点的节点
     */
    public static <T extends Treeable> List<T> buildTree(List<T> vos) {
        return TreeCoreConstructor.constructTree(ofListForInterface(vos));
    }

    /**
     * 构造一个代理节点
     *
     * @param t   原对象
     * @param <T> 节点类型
     * @return 代理节点
     */
    public static <T extends Treeable> List<TreeNodeProxy<T>> ofListForInterface(List<T> t) {
        if (null == t || t.isEmpty()) {
            return Collections.emptyList();
        }
        TreeNodeWrapper<T> wrapper = new TreeNodeWrapperInterface<>();
        return TreeNodeProxy.ofList(t, wrapper);
    }

    /**
     * 构造一个代理节点
     *
     * @param t   原对象
     * @param <T> 节点类型
     * @return 代理节点
     */
    public static <T> List<TreeNodeProxy<T>> ofListForAnnotation(List<T> t) {
        if (null == t || t.isEmpty()) {
            return Collections.emptyList();
        }
        TreeNodeWrapper<T> wrapper = TreeNodeWrapperAnnotation.from(t.get(0));
        return TreeNodeProxy.ofList(t, wrapper);
    }
}
