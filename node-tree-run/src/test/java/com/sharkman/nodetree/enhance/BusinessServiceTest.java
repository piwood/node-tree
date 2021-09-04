package com.sharkman.nodetree.enhance;

import com.sharkman.nodetree.core.TreeUtil;
import com.sharkman.nodetree.core.Treeable;
import com.sharkman.nodetree.runner.CommonTreeNode;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BusinessServiceTest {

    private final BusinessService businessService;

    /**
     * 测试实现 treeable 接口类，固定pid + 动态pid
     */
    @Test
    void findAllNodes() {
        int nodesCount = 10;
        List<Treeable> result = businessService.findAllNodes(nodesCount);
        assertNotNull(result);

        assertEquals(1, result.size());
        assertEquals(nodesCount, TreeUtil.countNodes(result));

        // ------------
        result = businessService.findAllNodes(nodesCount, "-");
        assertNotNull(result);

        assertEquals(1, result.size());
        assertEquals(nodesCount, TreeUtil.countNodes(result));

        // ------- 不指定
        result = businessService.findAllNodesNoSign(nodesCount, "-");
        assertNotNull(result);

        assertEquals(2, result.size());
        assertEquals(nodesCount + 1, TreeUtil.countNodes(result));
    }

    /**
     * 测试实现 treeable 接口类，固定id + 动态id
     */
    @Test
    void findSpecNodes() {
        int nodesCount = 10;
        List<Treeable> result = businessService.findSpecNodes(nodesCount);
        assertNotNull(result);

        assertEquals(1, result.size());
        assertEquals(nodesCount, TreeUtil.countNodes(result));

        // ------------
        result = businessService.findSpecNodes(nodesCount, "0");
        assertNotNull(result);

        assertEquals(1, result.size());
        assertEquals(nodesCount, TreeUtil.countNodes(result));
    }

    /**
     * 测试注解形式，固定pid + 动态pid
     */
    @Test
    void findAllNodesForAnnotation() {
        int nodesCount = 10;
        List<CommonTreeNode> result = businessService.findAllNodesForAnnotation(nodesCount);
        assertNotNull(result);

        assertEquals(1, result.size());
        assertEquals(nodesCount, countNodes(result));

        // ------------
        result = businessService.findAllNodesForAnnotation(nodesCount, "-");
        assertNotNull(result);

        assertEquals(1, result.size());
        assertEquals(nodesCount, countNodes(result));

        // ------- 不指定
        result = businessService.findAllNodesForAnnotationNoSign(nodesCount, "-");
        assertNotNull(result);

        assertEquals(2, result.size());
        assertEquals(nodesCount + 1, countNodes(result));
    }

    /**
     * 计算树上有多少个节点
     *
     * @param trees 数结构
     * @return 节点数量
     */
    private int countNodes(List<CommonTreeNode> trees) {
        if (null == trees || trees.isEmpty()) {
            return 0;
        }
        Queue<CommonTreeNode> queue = new LinkedList<>(trees);
        int count = 0;
        while (!queue.isEmpty()) {
            CommonTreeNode current = queue.poll();
            count++;

            List<CommonTreeNode> children;
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
}