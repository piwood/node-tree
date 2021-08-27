package com.sharkman.nodetree.enhance;

import com.sharkman.nodetree.annotation.NodeTree;
import com.sharkman.nodetree.core.Treeable;
import com.sharkman.nodetree.runner.AutoData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * BusinessService
 * 业务服务模拟类
 *
 * @author yanpengyu
 * @since 1.0
 * 2021/4/12 12:13 PM
 */
@Service
@RequiredArgsConstructor
public class BusinessService {

    /**
     * 模拟查找所有节点
     *
     * @param nodesCount 节点数量
     * @return 所有节点
     */
    @NodeTree(isPidNull = true)
    public List<Treeable> findAllNodes(int nodesCount) {
        int maxChild = 2;
        return AutoData.makeRandomNodes(nodesCount, maxChild, "0");
    }
}
