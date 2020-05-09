package com.sharkman.nodetreerun.runner;

import com.sharkman.commons.tree.TreeUtil;
import com.sharkman.commons.tree.Treeable;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AutoDataTest {

    @Test
    void makeRandomNodes() {
        long startTime = System.currentTimeMillis();
        String rootPid = "0";
        int maxChild = 7;
        List<Treeable> nodes = AutoData.makeRandomNodes(10000, maxChild, rootPid);

        System.out.println("current times : " + (System.currentTimeMillis() - startTime));
        Treeable root = TreeUtil.buildTreeOfRootPId(nodes, rootPid);
        assertEquals(nodes.size(), countNodes(Collections.singletonList(root)));
        assertTrue(isMaxChildCountValid(Collections.singletonList(root), maxChild));
    }

    private boolean isMaxChildCountValid(List<Treeable> trees, int max) {
        if (null == trees || trees.isEmpty()) {
            return false;
        }

        Queue<Treeable> queue = new LinkedList<>(trees);
        while (!queue.isEmpty()) {
            Treeable current = queue.poll();
            if (Optional.ofNullable(current.getChildren())
                    .map(List::size)
                    .orElse(0) > max) {
                return false;
            }
            List<Treeable> children = current.getChildren();
            if (null != children && !children.isEmpty()) {
                queue.addAll(children);
            }
        }
        return true;
    }

    private int countNodes(List<Treeable> trees) {
        if (null == trees || trees.isEmpty()) {
            return 0;
        }

        Queue<Treeable> queue = new LinkedList<>(trees);
        int count = 0;
        while (!queue.isEmpty()) {
            Treeable current = queue.poll();
            System.out.println(current);
            count++;
            List<Treeable> children = current.getChildren();
            if (null != children && !children.isEmpty()) {
                queue.addAll(children);
            }
        }
        return count;
    }
}
