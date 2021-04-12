package com.sharkman.nodetree.aspect;

import com.sharkman.nodetree.annotation.NodeTree;
import com.sharkman.nodetree.core.TreeUtil;
import com.sharkman.nodetree.core.Treeable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * NodeTreeAspect
 * 节点树切面
 *
 * @author yanpengyu
 * @since 1.0
 * 2021/4/8 8:58 PM
 */
@Aspect
@Component
public class NodeTreeAspect {
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
            return TreeUtil.buildTreeOfRootPId(results, null);
        }

        return TreeUtil.buildTreeOfRootPId(results, pid);
    }
}
