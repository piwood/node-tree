package com.sharkman.nodetree.core;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p> Description:构建后结果</p>
 * <p> CreationTime: 2021/10/20 11:10
 *
 * @author piwood
 * @version 1.0
 * @since 1.1.4
 */
@Getter
@Setter
final class ConstructedResult<T> {
    private List<T> maybeRoot;

}
