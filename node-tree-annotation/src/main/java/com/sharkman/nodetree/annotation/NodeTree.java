package com.sharkman.nodetree.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> Description:树节点注解</p>
 * <p> CreationTime: 2020/10/9 11:31
 * <br>Copyright: &copy;2020 <a href="http://www.sharkman.com">Sharkman</a>
 * <br>Email: <a href="mailto:526478642@qq.com">526478642@qq.com</a></p>
 *
 * @author yanpengyu
 * @version 1.0
 * @since 1.1.2
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NodeTree {
    /**
     * 直接取根节点id, 如果设置了此属性，则以此为准
     *
     * @return 根节点id
     */
    String id() default "";

    /**
     * 直接取根节点父id，若未设置id，则取此属性
     *
     * @return 根节点父id
     */
    String pid() default "";

    /**
     * 当根节点pid为 null 时，将此值设置为true
     *
     * @return pid 是否为 null
     */
    boolean isPidNull() default false;
}
