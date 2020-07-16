package com.sharkman.nodetreerun.runner;

import com.sharkman.commons.tree.Treeable;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * <p> Description:遍历树工具类</p>
 * <p> CreationTime: 2020/7/16 11:45
 * <br>Copyright: &copy;2020 <a href="http://www.thunisoft.com">Thunisoft</a>
 * <br>Email: <a href="mailto:yanpengyu@thunisoft.com">yanpengyu@thunisoft.com</a></p>
 *
 * @author yanpengyu
 * @since 1.0
 */
public final class TreeTraverseUtil {
    private TreeTraverseUtil() {
    }

    /**
     * 深度递归遍历
     *
     * @param treeNodes 数节点
     */
    public static void deepTraverseOfRecursive(List<Treeable> treeNodes) {
        if (CollectionUtils.isEmpty(treeNodes)) {
            return;
        }
        for (Treeable node : treeNodes) {
            System.out.println(node);
            deepTraverseOfRecursive(node.getChildren());
        }
    }

    /**
     * 广度递归遍历
     *
     * @param treeNodes 数节点
     */
    public static void breadthTraverseOfRecursive(List<Treeable> treeNodes) {
        if (CollectionUtils.isEmpty(treeNodes)) {
            return;
        }
        List<Treeable> allChildren = new ArrayList<>();
        for (Treeable node : treeNodes) {
            System.out.println(node);
            List<Treeable> children = node.getChildren();
            if (CollectionUtils.isNotEmpty(children)) {
                allChildren.addAll(children);
            }
        }
        breadthTraverseOfRecursive(allChildren);
    }

    /**
     * 深度遍历
     *
     * @param treeNodes 数节点
     */
    public static void deepTraverse(List<Treeable> treeNodes) {
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
            System.out.println(node);
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
     */
    public static void breadthTraverse(List<Treeable> treeNodes) {
        if (CollectionUtils.isEmpty(treeNodes)) {
            return;
        }
        Deque<Treeable> queue = new LinkedList<>();
        queue.addAll(treeNodes);
        while (!queue.isEmpty()) {
            Treeable node = queue.poll();
            // 输出内容，各种操作
            System.out.println(node);
            List<Treeable> children = node.getChildren();
            if (CollectionUtils.isNotEmpty(children)) {
                queue.addAll(children);
            }
        }
    }
}
