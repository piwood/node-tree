package com.sharkman.nodetree.aspect;

import com.sharkman.nodetree.core.TreeUtilForAnnotation;

import java.util.List;

/**
 * <p> Description:注解型树节点创建类</p>
 * <p> CreationTime: 2021/9/4 13:54
 *
 * @author piwood
 * @version 1.0
 * @since 1.1.4
 */
final class TreeCreatorForAnnotation implements TreeCreator {
    private TreeCreatorForAnnotation() {
    }

    private static final TreeCreatorForAnnotation INSTANCE = new TreeCreatorForAnnotation();

    /**
     * 获取实例
     *
     * @return 实例
     */
    static TreeCreator getInstance() {
        return INSTANCE;
    }

    @Override
    public Object buildTreeOfRootId(List<Object> vos, String id) {
        return TreeUtilForAnnotation.buildTreeOfRootId(vos, id);
    }

    @Override
    public List<Object> buildTreeOfRootPIdForList(List<Object> vos, String pid) {
        return TreeUtilForAnnotation.buildTreeOfRootPIdForList(vos, pid);
    }

    @Override
    public List<Object> buildTree(List<Object> vos) {
        return TreeUtilForAnnotation.buildTree(vos);
    }
}
