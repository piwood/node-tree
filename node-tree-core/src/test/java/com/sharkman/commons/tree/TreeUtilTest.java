package com.sharkman.commons.tree;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
class TreeUtilTest {

    /**
     * 根据id获取根节点
     */
    @Test
    void buildTreeOfRootId() {
        List<Tree> trees = oneRootTrees();
        Tree root = TreeUtil.buildTreeOfRootId(trees, "1");
        assertEquals("1", root.getId());
    }

    /**
     * 根据id获取根节点
     */
    @Test
    void countNodesOfRootId() {
        List<Tree> trees = oneRootTrees();
        Tree root = TreeUtil.buildTreeOfRootId(trees, "1");
        assertEquals(countNodes(Collections.singletonList(root)), trees.size());
    }

    /**
     * 根据pid获取根节点
     */
    @Test
    void buildTreeOfPid() {
        List<Tree> trees = oneRootTrees();
        Tree root = TreeUtil.buildTreeOfRootPId(trees, null);
        Tree expect = new Tree("1", null);
        assertEquals(expect, root);
        assertEquals(countNodes(Collections.singletonList(root)), trees.size());
        Tree rootOrigin = root;
        root = TreeUtil.buildTreeOfRootPId(trees,"x");
        // 避免对同数据多次执行，多次执行子节点将会重复
        assertEquals(4, rootOrigin.getChildren().size());
        assertNull(root);
        // 根节点不为null
        trees = oneRootTrees2();
        root = TreeUtil.buildTreeOfRootPId(trees, "0");
        expect = new Tree("1", "0");
        assertEquals(expect, root);
        assertEquals(countNodes(Collections.singletonList(root)), trees.size());
    }

    /**
     * 根据 pid 获取的根节点有多个
     */
    @Test
    void buildMultiRootTreeOfPid() {
        List<Tree> trees = multiRootTrees();
        List<Tree> root = TreeUtil.buildTreeOfRootPIdForList(trees, "0");
        assertEquals(3, root.size());
        Tree exceptRoot1 = new Tree("1", "0");
        Tree exceptRoot2 = new Tree("01", "0");
        Tree exceptRoot3 = new Tree("11", "0");

        assertTrue(root.contains(exceptRoot1));
        assertTrue(root.contains(exceptRoot2));
        assertTrue(root.contains(exceptRoot3));
        assertEquals(countNodes(root), trees.size() - 1);
    }

    int countNodes(List<Tree> trees) {
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

    private List<Tree> oneRootTrees() {
        List<Tree> trees = new ArrayList<>();
        trees.add(new Tree("1", null));
        trees.add(new Tree("2", "1"));
        trees.add(new Tree("3", "2"));
        trees.add(new Tree("2.1", "1"));
        return trees;
    }

    private List<Tree> oneRootTrees2() {
        List<Tree> trees = new ArrayList<>();
        trees.add(new Tree("1", "0"));
        trees.add(new Tree("2", "1"));
        trees.add(new Tree("3", "2"));
        trees.add(new Tree("2.1", "1"));
        return trees;
    }

    private List<Tree> multiRootTrees() {
        List<Tree> trees = new ArrayList<>();
        trees.add(new Tree("1", "0"));
        trees.add(new Tree("2", "1"));
        trees.add(new Tree("3", "2"));
        trees.add(new Tree("2.1", "1"));

        trees.add(new Tree("01", "0"));
        trees.add(new Tree("02", "01"));
        trees.add(new Tree("03", "02"));
        trees.add(new Tree("02.1", "01"));
        trees.add(new Tree("11", "0"));
        trees.add(new Tree("xx", "xx"));
        return trees;
    }


}

class Tree implements Treeable {
    public Tree(String id, String pId) {
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
        Tree tree = (Tree) o;
        return Objects.equals(id, tree.id) &&
                Objects.equals(pId, tree.pId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pId);
    }

    @Override
    public String toString() {
        return "Tree{" +
                "id='" + id + '\'' +
                ", pId='" + pId + '\'' +
                ", children size:" + (null == children?0:children.size()) +
                '}';
    }
}
