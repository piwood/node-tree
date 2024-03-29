package com.sharkman.nodetree.core;

import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.function.Predicate;

/**
 * <p> Description:遍历树工具类</p>
 * <p> CreationTime: 2020/7/16 11:45
 * <br>Copyright: &copy;2020 <a href="http://www.sharkman.com">Sharkman</a>
 * <br>Email: <a href="mailto:526478642@qq.com">526478642@qq.com</a></p>
 *
 * @author yanpengyu
 * @version 1.0
 * @since 1.1.2
 */
public final class TreeTraverseUtil {
    private TreeTraverseUtil() throws IllegalAccessException {
        throw new IllegalAccessException("非法访问构造函数");
    }

    /**
     * 深度递归遍历
     *
     * @param treeNodes 数节点
     * @param isBreak  针对每个节点执行的方法,返回 true 则停止遍历
     */
    public static void deepTraverseOfRecursive(List<Treeable> treeNodes, Predicate<Treeable> isBreak) {
        if (CollectionUtils.isEmpty(treeNodes)) {
            return;
        }
        for (Treeable node : treeNodes) {
            if (Boolean.TRUE.equals(isBreak.test(node))) {
                break;
            }
            deepTraverseOfRecursive(node.getChildren(), isBreak);
        }
    }

    /**
     * 广度递归遍历
     *
     * @param treeNodes 数节点
     * @param isBreak  针对每个节点执行的方法,返回 true 则停止遍历
     */
    public static void breadthTraverseOfRecursive(List<Treeable> treeNodes, Predicate<Treeable> isBreak) {
        if (CollectionUtils.isEmpty(treeNodes)) {
            return;
        }
        List<Treeable> allChildren = new ArrayList<>();
        for (Treeable node : treeNodes) {
            if (Boolean.TRUE.equals(isBreak.test(node))) {
                break;
            }
            List<Treeable> children = node.getChildren();
            if (CollectionUtils.isNotEmpty(children)) {
                allChildren.addAll(children);
            }
        }
        breadthTraverseOfRecursive(allChildren, isBreak);
    }

    /**
     * 深度遍历
     *
     * @param treeNodes 数节点
     * @param isBreak  针对每个节点执行的方法,返回 true 则停止遍历
     */
    public static void deepTraverse(List<Treeable> treeNodes, Predicate<Treeable> isBreak) {
        if (CollectionUtils.isEmpty(treeNodes)) {
            return;
        }
        Deque<Treeable> stack = new LinkedList<>();
        for (Treeable node : treeNodes) {
            stack.push(node);
        }
        while (!stack.isEmpty()) {
            Treeable node = stack.pop();
            // 输出内容，各种操作
            if (Boolean.TRUE.equals(isBreak.test(node))) {
                break;
            }
            List<Treeable> children = node.getChildren();
            if (CollectionUtils.isNotEmpty(children)) {
                for (Treeable child : children) {
                    stack.push(child);
                }
            }
        }
    }

    /**
     * 广度遍历
     *
     * @param treeNodes 数节点
     * @param isBreak  针对每个节点执行的方法,返回 true 则停止遍历
     */
    public static void breadthTraverse(List<Treeable> treeNodes, Predicate<Treeable> isBreak) {
        if (CollectionUtils.isEmpty(treeNodes)) {
            return;
        }
        Deque<Treeable> queue = new LinkedList<>(treeNodes);
        while (!queue.isEmpty()) {
            Treeable node = queue.poll();
            // 输出内容，各种操作
            if (Boolean.TRUE.equals(isBreak.test(node))) {
                break;
            }
            List<Treeable> children = Optional.ofNullable(node)
                    .map(Treeable::getChildren)
                    .orElse(Collections.emptyList());
            if (CollectionUtils.isNotEmpty(children)) {
                queue.addAll(children);
            }
        }
    }

    /**
     * 计算树上有多少个节点
     *
     * @param trees 数结构
     * @return 节点数量
     */
    public static <T> int countNodes(List<T> trees) {
        if (null == trees || trees.isEmpty()) {
            return 0;
        }
        return TreeCoreConstructor.countNodes(trees, TreeNodeProxy.createNodeWrapper(trees.get(0)));
    }
}
