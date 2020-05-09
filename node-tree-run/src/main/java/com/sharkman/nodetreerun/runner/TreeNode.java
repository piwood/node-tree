package com.sharkman.nodetreerun.runner;

import com.sharkman.commons.tree.Treeable;

import java.util.List;
import java.util.Objects;

/**
 * <p> Description:TreeNode</p>
 * <p> CreationTime: 2020/5/9 9:55
 * <br>Copyright: &copy;2020 <a href="http://www.thunisoft.com">Thunisoft</a>
 * <br>Email: <a href="mailto:yanpengyu@thunisoft.com">yanpengyu@thunisoft.com</a></p>
 *
 * @author yanpengyu
 * @since 1.0
 */
final class TreeNode implements Treeable {
    private String id;
    private String pId;
    private List<Treeable> children;

    TreeNode(String id, String pId) {
        this.id = id;
        this.pId = pId;
    }

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
        TreeNode treeNode = (TreeNode) o;
        return Objects.equals(id, treeNode.id) &&
                Objects.equals(pId, treeNode.pId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pId);
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "id='" + id + '\'' +
                ", pId='" + pId + '\'' +
                ", children size:" + (null == children ? 0 : children.size()) +
                '}';
    }
}
