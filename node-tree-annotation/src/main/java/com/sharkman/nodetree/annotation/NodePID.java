package com.sharkman.nodetree.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> Description:节点父id</p>
 * <p> CreationTime: 2020/10/9 11:49
 * <br>Copyright: &copy;2020 <a href="http://www.thunisoft.com">Thunisoft</a>
 * <br>Email: <a href="mailto:yanpengyu@thunisoft.com">yanpengyu@thunisoft.com</a></p>
 *
 * @author yanpengyu
 * @version 1.0
 * @since 1.1.2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface NodePID {
}
