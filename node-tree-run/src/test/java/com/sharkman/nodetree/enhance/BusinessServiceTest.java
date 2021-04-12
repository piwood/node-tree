package com.sharkman.nodetree.enhance;

import com.sharkman.nodetree.core.TreeUtil;
import com.sharkman.nodetree.core.Treeable;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BusinessServiceTest {

    private final BusinessService businessService;

    @Test
    void findAllNodes() {
        int nodesCount = 10;
        List<Treeable> result = businessService.findAllNodes(nodesCount);

        assertNotNull(result);

        assertEquals(1, result.size());
        assertEquals(nodesCount, TreeUtil.countNodes(result));
    }
}