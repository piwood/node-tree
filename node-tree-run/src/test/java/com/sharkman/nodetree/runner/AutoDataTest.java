package com.sharkman.nodetree.runner;

import com.sharkman.nodetree.core.TreeUtil;
import com.sharkman.nodetree.core.Treeable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.*;

import static com.sharkman.nodetree.core.TreeTraverseUtil.countNodes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class AutoDataTest {

    @Test
    void makeRandomNodes() {
        String rootPid = "0";
        int nodesCount = 10;
        int maxChild = 2;
        long startTime = System.currentTimeMillis();
        List<Treeable> nodes = AutoData.makeRandomNodes(nodesCount, maxChild, rootPid);
        log.info("init data cost : " + (System.currentTimeMillis() - startTime) + "ms");
        startTime = System.currentTimeMillis();
        Treeable root = TreeUtil.buildTreeOfRootPId(nodes, rootPid);
        log.info("construct tree cost : " + (System.currentTimeMillis() - startTime) + "ms");
        assertEquals(nodesCount, countNodes(Collections.singletonList(root)));
        assertTrue(isMaxChildCountValid(Collections.singletonList(root), maxChild));
    }

    private boolean isMaxChildCountValid(List<Treeable> trees, int max) {
        if (null == trees || trees.isEmpty()) {
            return false;
        }
        Queue<Treeable> queue = new LinkedList<>(trees);
        while (!queue.isEmpty()) {
            Treeable current = queue.poll();
            if (Optional.ofNullable(current).map(Treeable::getChildren)
                    .map(List::size)
                    .orElse(0) > max) {
                return false;
            }
            List<Treeable> children = Optional.ofNullable(current)
                    .map(Treeable::getChildren)
                    .orElse(Collections.emptyList());
            if (!children.isEmpty()) {
                queue.addAll(children);
            }
        }
        return true;
    }
}
