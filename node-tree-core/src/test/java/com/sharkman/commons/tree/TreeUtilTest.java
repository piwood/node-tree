package com.sharkman.commons.tree;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TreeUtilTest {

    @Test
    void buildTreeOfRootId() {
        List<Tree> trees = new ArrayList<>();
        trees.add(new Tree("1", null));
        trees.add(new Tree("2", "1"));
        trees.add(new Tree("3", "2"));
        trees.add(new Tree("2.1", "1"));
        Tree root = TreeUtil.buildTreeOfRootId(trees, "1");
        assertEquals(root.getId(), "1");
    }

    private static class Tree implements Treeable{
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
    }
}
