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
    String getId();
    String getPId();
    List<Treeable> getChildren();
    void setChildren(List<Treeable> children);
}
