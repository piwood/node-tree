package com.sharkman.nodetree.aspect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p> Description:节点树配置</p>
 * <p> CreationTime: 2021/8/27 16:21
 * <br>Email: <a href="mailto:yanpengyu@thunisoft.com">yanpengyu@thunisoft.com</a></p>
 *
 * @author yanpengyu
 * @version 1.0
 * @since 2.1
 */
@Configuration
public class NodeTreeConfig {

    /**
     * 注入Bean
     *
     * @return 切面bean
     */
    @Bean
    public NodeTreeAspect nodeTreeAspect() {
        return new NodeTreeAspect();
    }
}
