package com.sharkman.nodetree.enhance;

import com.sharkman.nodetree.annotation.RootPID;
import lombok.Data;

/**
 * <p> Description:参数</p>
 * <p> CreationTime: 2021/9/4 16:18
 *
 * @author piwood
 * @version 1.0
 * @since 1.1.4
 */
@Data
public class ParamsOfPid {
    private String id;
    @RootPID
    private String pid;
}
