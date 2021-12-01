package com.sharkman.nodetree.aspect;

import com.sharkman.nodetree.core.TreeUtil;
import com.sharkman.nodetree.core.Treeable;

import java.util.List;

/**
 * <p> Description:注解型树节点创建类</p>
 * <p> CreationTime: 2021/9/4 13:54
 *
 * @author piwood
 * @version 1.0
 * @since 1.1.4
 */
final class TreeCreatorForInterface implements TreeCreator {
    private TreeCreatorForInterface() {
    }

    private static final TreeCreatorForInterface INSTANCE = new TreeCreatorForInterface();

    /**
     * 获取实例
     *
     * @return 实例
     */
    static TreeCreator getInstance() {
        return INSTANCE;
    }

    @Override
    @SuppressWarnings("unchecked") // 方法包内可见，保证转化成功
    public Object buildTreeOfRootId(List<Object> vos, String id) {
        List<Treeable> actualObj = (List<Treeable>) (Object) vos;
        return TreeUtil.buildTreeOfRootId(actualObj, id);
    }

    @Override
    @SuppressWarnings("unchecked") // 方法包内可见，保证转化成功
    public List<Object> buildTreeOfRootPIdForList(List<Object> vos, String pid) {
        List<Treeable> actualObj = (List<Treeable>) (Object) vos;
        return (List<Object>) (Object) TreeUtil.buildTreeOfRootPIdForList(actualObj, pid);
    }

    @Override
    @SuppressWarnings("unchecked") // 方法包内可见，保证转化成功
    public List<Object> buildTree(List<Object> vos) {
        List<Treeable> actualObj = (List<Treeable>) (Object) vos;
        return (List<Object>) (Object) TreeUtil.constructTree(actualObj);
    }
}
