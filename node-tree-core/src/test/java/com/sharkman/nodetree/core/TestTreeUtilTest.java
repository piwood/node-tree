package com.sharkman.nodetree.core;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.sharkman.nodetree.core.TreeUtil.countNodes;
import static org.junit.jupiter.api.Assertions.*;

class TestTreeUtilTest {

    /**
     * 根据id获取根节点
     */
    @Test
    void buildTreeOfRootId() {
        List<TestTree> trees = oneRootTrees();
        TestTree root = TreeUtil.buildTreeOfRootId(trees, "1");
        assertEquals("1", root.getId());
    }

    /**
     * 根据id获取根节点
     */
    @Test
    void countNodesOfRootId() {
        List<TestTree> trees = oneRootTrees();
        TestTree root = TreeUtil.buildTreeOfRootId(trees, "1");
        assertEquals(countNodes(Collections.singletonList(root)), trees.size());
    }

    /**
     * 根据pid获取根节点
     */
    @Test
    void buildTreeOfPid() {
        List<TestTree> trees = oneRootTrees();
        TestTree root = TreeUtil.buildTreeOfRootPId(trees, null);
        TestTree expect = new TestTree("1", null);
        assertEquals(expect, root);
        assertEquals(countNodes(Collections.singletonList(root)), trees.size());
        TestTree rootOrigin = root;
        root = TreeUtil.buildTreeOfRootPId(trees,"x");
        // 避免对同数据多次执行，多次执行子节点将会重复
        assertEquals(4, rootOrigin.getChildren().size());
        assertNull(root);
        // 根节点不为null
        trees = oneRootTrees2();
        root = TreeUtil.buildTreeOfRootPId(trees, "0");
        expect = new TestTree("1", "0");
        assertEquals(expect, root);
        assertEquals(countNodes(Collections.singletonList(root)), trees.size());
    }

    /**
     * 根据 pid 获取的根节点有多个
     */
    @Test
    void buildMultiRootTreeOfPid() {
        List<TestTree> trees = multiRootTrees();
        List<TestTree> root = TreeUtil.buildTreeOfRootPIdForList(trees, "0");
        assertEquals(3, root.size());
        TestTree exceptRoot1 = new TestTree("1", "0");
        TestTree exceptRoot2 = new TestTree("01", "0");
        TestTree exceptRoot3 = new TestTree("11", "0");

        assertTrue(root.contains(exceptRoot1));
        assertTrue(root.contains(exceptRoot2));
        assertTrue(root.contains(exceptRoot3));
        assertEquals(countNodes(root), trees.size() - 1);
    }


    private List<TestTree> oneRootTrees() {
        List<TestTree> trees = new ArrayList<>();
        trees.add(new TestTree("1", null));
        trees.add(new TestTree("2", "1"));
        trees.add(new TestTree("3", "2"));
        trees.add(new TestTree("2.1", "1"));
        return trees;
    }

    private List<TestTree> oneRootTrees2() {
        List<TestTree> trees = new ArrayList<>();
        trees.add(new TestTree("1", "0"));
        trees.add(new TestTree("2", "1"));
        trees.add(new TestTree("3", "2"));
        trees.add(new TestTree("2.1", "1"));
        return trees;
    }

    private List<TestTree> multiRootTrees() {
        List<TestTree> trees = new ArrayList<>();
        trees.add(new TestTree("1", "0"));
        trees.add(new TestTree("2", "1"));
        trees.add(new TestTree("3", "2"));
        trees.add(new TestTree("2.1", "1"));

        trees.add(new TestTree("01", "0"));
        trees.add(new TestTree("02", "01"));
        trees.add(new TestTree("03", "02"));
        trees.add(new TestTree("02.1", "01"));
        trees.add(new TestTree("11", "0"));
        trees.add(new TestTree("xx", "xx"));
        return trees;
    }


}

class TestTree implements Treeable {
    TestTree(String id, String pId) {
        this.id = id;
        this.pId = pId;
    }

    private String id;
    private String pId;
    private List<Treeable> children;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getPId() {
        return pId;
    }

    @Override
    public List<Treeable> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<Treeable> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestTree tree = (TestTree) o;
        return Objects.equals(id, tree.id) &&
                Objects.equals(pId, tree.pId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pId);
    }

    @Override
    public String toString() {
        return "TestTree{" +
                "id='" + id + '\'' +
                ", pId='" + pId + '\'' +
                ", children size:" + (null == children?0:children.size()) +
                '}';
    }
}
