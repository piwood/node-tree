package com.sharkman.nodetree.runner;

import com.sharkman.nodetree.annotation.NodeChildren;
import com.sharkman.nodetree.annotation.NodeID;
import com.sharkman.nodetree.annotation.NodePID;
import com.sharkman.nodetree.core.Treeable;
import lombok.Data;

import java.util.List;

/**
 * <p> Description:</p>
 * <p> CreationTime: 2020/5/9 9:55
 * <br>Copyright: &copy;2020 <a href="http://www.sharkman.com">Sharkman</a>
 * <br>Email: <a href="mailto:526478642@qq.com">526478642@qq.com</a></p>
 *
 * @author yanpengyu
 * @version 1.0
 * @since 1.0
 */
@Data
public final class CommonTreeNode {
    @NodeID
    private String id;
    @NodePID
    private String pId;
    @NodeChildren
    private List<CommonTreeNode> children;

    public CommonTreeNode(String id, String pId) {
        this.id = id;
        this.pId = pId;
    }

    public static CommonTreeNode of(Treeable node) {
        return new CommonTreeNode(node.getId(), node.getPId());
    }
}
