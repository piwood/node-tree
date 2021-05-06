package com.sharkman.nodetree.aspect;

import com.sharkman.nodetree.annotation.NodeTree;
import com.sharkman.nodetree.core.TreeUtil;
import com.sharkman.nodetree.core.Treeable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 节点树切面
 * 用于树节点基于 {@link Treeable} 接口实现
 *
 * @author yanpengyu
 * @see NodeTreeAspectForAnnotation 用于树节点基于注解实现
 * 2021/4/8 8:58 PM
 * @since 1.1.2
 */
@Aspect
@Component
@ConditionalOnProperty(name = "node-tree.type", havingValue = "interface")
public class NodeTreeAspectForInterface {
    /**
     * 解析方法返回值，生成树结构;
     *
     * @param joinPoint 切点代理方法
     * @param nodeTree  节点树注解
     * @throws Throwable 方法执行异常，则抛出异常
     */
    @Around(value = "@annotation(nodeTree)")
    public Object constructTree(ProceedingJoinPoint joinPoint, NodeTree nodeTree) throws Throwable {
        // 可以检查是不是list
        @SuppressWarnings("unchecked")
        List<Treeable> results = (List<Treeable>) joinPoint.proceed();
        String id = nodeTree.id();
        String pid = nodeTree.pid();
        if (!"".equals(id)) {
            return Collections.singletonList(TreeUtil.buildTreeOfRootId(results, id));
        }
        if ("".equals(pid)) {
            return TreeUtil.buildTreeOfRootPIdForList(results, null);
        }

        return TreeUtil.buildTreeOfRootPIdForList(results, pid);
    }
}
