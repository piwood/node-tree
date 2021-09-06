package com.sharkman.nodetree.enhance;

import com.sharkman.nodetree.annotation.NodeTree;
import com.sharkman.nodetree.annotation.RootID;
import com.sharkman.nodetree.annotation.RootPID;
import com.sharkman.nodetree.core.Treeable;
import com.sharkman.nodetree.runner.AutoData;
import com.sharkman.nodetree.runner.CommonTreeNode;
import com.sharkman.nodetree.runner.TreeNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    @NodeTree(pid = "-")
    public List<Treeable> findAllNodes(int nodesCount) {
        return mockData(nodesCount);
    }

    /**
     * 模拟查找所有节点
     *
     * @param nodesCount 节点数量
     * @return 所有节点
     */
    @NodeTree(id = "0")
    public List<Treeable> findSpecNodes(int nodesCount) {
        return mockData(nodesCount);
    }

    private List<Treeable> mockData(int nodesCount) {
        return mockDataForPid(nodesCount, "-");
    }

    /**
     * 模拟查找所有节点
     *
     * @param nodesCount 节点数量
     * @return 所有节点
     */
    @NodeTree
    public List<Treeable> findSpecNodes(int nodesCount, @RootID String id) {
        return mockData(nodesCount);
    }

    /**
     * 模拟查找所有节点
     *
     * @param nodesCount 节点数量
     * @return 所有节点
     */
    @NodeTree
    public List<Treeable> findSpecNodesForParams(int nodesCount, ParamsOfID paramsOfID) {
        return mockData(nodesCount);
    }

    /**
     * 模拟查找所有节点
     *
     * @param nodesCount 节点数量
     * @return 所有节点
     */
    @NodeTree
    public List<Treeable> findAllNodesForParam(int nodesCount, ParamsOfPid paramsOfPid) {
        return mockData(nodesCount);
    }

    /**
     * 模拟查找所有节点
     *
     * @param nodesCount 节点数量
     * @return 所有节点
     */
    @NodeTree
    public List<Treeable> findAllNodes(int nodesCount, @RootPID String pid) {
        return mockDataForPid(nodesCount, pid);
    }

    /**
     * 模拟查找所有节点
     *
     * @param nodesCount 节点数量
     * @return 所有节点
     */
    @NodeTree
    public List<Treeable> findAllNodesNoSign(int nodesCount, String pid) {
        return mockDataForPid(nodesCount, pid);
    }

    private List<Treeable> mockDataForPid(int nodesCount, String pid) {
        int maxChild = 2;
        List<Treeable> result = AutoData.makeRandomNodes(nodesCount, maxChild, pid);
        // 额外加个根节点
        result.add(new TreeNode("x", "y"));
        return result;
    }


    /**
     * 模拟查找所有节点
     *
     * @param nodesCount 节点数量
     * @return 所有节点
     */
    @NodeTree(pid = "-")
    public List<CommonTreeNode> findAllNodesForAnnotation(int nodesCount) {
        return mockDataForAnno(nodesCount, "-");
    }

    /**
     * 模拟查找所有节点
     *
     * @param nodesCount 节点数量
     * @return 所有节点
     */
    @NodeTree
    public List<CommonTreeNode> findAllNodesForAnnotation(int nodesCount, @RootPID String pid) {
        return mockDataForAnno(nodesCount, pid);
    }

    /**
     * 模拟查找所有节点
     *
     * @param nodesCount 节点数量
     * @return 所有节点
     */
    @NodeTree
    public List<CommonTreeNode> findAllNodesForAnnotationNoSign(int nodesCount, String pid) {
        return mockDataForAnno(nodesCount, pid);
    }

    private List<CommonTreeNode> mockDataForAnno(int nodesCount, String pid) {
        int maxChild = 2;
        List<CommonTreeNode> result = AutoData.makeRandomNodes(nodesCount, maxChild, pid)
                .stream().map(CommonTreeNode::of).collect(Collectors.toList());
        // 额外加个根节点
        result.add(new CommonTreeNode("x", "y"));
        return result;
    }
}
