package com.sharkman.nodetree.aspect;

import com.sharkman.nodetree.annotation.NodeTree;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 节点树切面
 * 用于树节点基于 {@link com.sharkman.nodetree.annotation.NodePID}
 * {@link com.sharkman.nodetree.annotation.NodeID}实现
 * @author yanpengyu
 * @since 1.1.3
 * @see NodeTreeAspectForInterface 用于树节点基于接口实现
 * 2021/5/6 2:58 PM
 */
@Slf4j
@Aspect
@Component
@ConditionalOnProperty(name = "node-tree.type", havingValue = "annotation", matchIfMissing = true)
public class NodeTreeAspectForAnnotation {
    /**
     * 解析方法返回值，生成树结构;
     *
     * @param joinPoint 切点代理方法
     * @param nodeTree  节点树注解
     * @throws Throwable 方法执行异常，则抛出异常
     */
    @Around(value = "@annotation(nodeTree)")
    public Object constructTree(ProceedingJoinPoint joinPoint, NodeTree nodeTree) throws Throwable {
        Object returnValue = joinPoint.proceed();
        if (null == returnValue) {
            return null;
        }
        if (!(returnValue instanceof List)) {
            log.error("不支持的类型：{}, 期望类型为 List ", returnValue.getClass().getName());
        }
        List<Object> results = (List<Object>) returnValue;

        return null;
    }
}
