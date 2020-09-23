package com.sharkman.nodetreerun.runner;

import com.sharkman.commons.tree.TreeTraverseUtil;
import com.sharkman.commons.tree.TreeUtil;
import com.sharkman.commons.tree.Treeable;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static com.sharkman.commons.tree.TreeUtil.countNodes;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestTreeTraverseUtilTest {

    @Test
    void deepTraverse() {
        Treeable root = getTree();
        long startTime = System.currentTimeMillis();
        assertDoesNotThrow(() -> TreeTraverseUtil.deepTraverse(
                Collections.singletonList(root), getOutputPredicate()));
        System.out.println("deep tree cost : " + (System.currentTimeMillis() - startTime) + "ms");
        startTime = System.currentTimeMillis();
        assertDoesNotThrow(() ->
                TreeTraverseUtil.deepTraverseOfRecursive(Collections.singletonList(root), getOutputPredicate()));
        System.out.println("deep recursive tree cost : " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private Predicate<Treeable> getOutputPredicate() {
        return n -> {
            System.out.println(n);
            return false;
        };
    }

    @Test
    void breadthTraverse() {
        Treeable root = getTree();
        long startTime = System.currentTimeMillis();
        assertDoesNotThrow(() ->
                TreeTraverseUtil.breadthTraverse(Collections.singletonList(root), getOutputPredicate()));
        System.out.println("breadth tree cost : " + (System.currentTimeMillis() - startTime) + "ms");
        startTime = System.currentTimeMillis();
        assertDoesNotThrow(() ->
                TreeTraverseUtil.breadthTraverseOfRecursive(Collections.singletonList(root), getOutputPredicate()));
        System.out.println("breadth recursive tree cost : " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private Treeable getTree() {
        String rootPid = "root";
        int nodesCount = 7;
        int maxChild = 3;
        long startTime = System.currentTimeMillis();
        List<Treeable> nodes = AutoData.makeRandomNodes(nodesCount, maxChild, rootPid);
        System.out.println("init data cost : " + (System.currentTimeMillis() - startTime) + "ms");
        startTime = System.currentTimeMillis();
        Treeable root = TreeUtil.buildTreeOfRootPId(nodes, rootPid);
        System.out.println("construct tree cost : " + (System.currentTimeMillis() - startTime) + "ms");
        assertEquals(nodesCount, countNodes(Collections.singletonList(root)));
        return root;
    }
}