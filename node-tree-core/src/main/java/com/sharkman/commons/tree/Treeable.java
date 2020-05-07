package com.sharkman.commons.tree;

import java.util.List;

/**
 * <p> Description:树接口，定义前台树节点方法</p>
 * <p> CreationTime: 2020/2/5 20:47
 * <br>Copyright: &copy;2020 <a href="http://www.thunisoft.com">Thunisoft</a>
 * <br>Email: <a href="mailto:yanpengyu@thunisoft.com">yanpengyu@thunisoft.com</a></p>
 *
 * @author yanpengyu
 * @since 1.0
 */
public interface Treeable {
    /**
     * 获取树节点 id
     * @return 树节点 id
     */
    String getId();

    /**
     * 获取树节点父节点 id
     * @return 父节点 id
     */
    String getPId();

    /**
     * 获取子节点
     * @return 孩子节点
     */
    List<Treeable> getChildren();

    /**
     * 设置子节点信息
     * @param children 子节点信息
     */
    void setChildren(List<Treeable> children);
}
