package com.sharkman.nodetree.runner;

import com.sharkman.nodetree.core.Treeable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <p> Description:AutoData</p>
 * <p> CreationTime: 2020/5/8 18:12
 * <br>Copyright: &copy;2020 <a href="http://www.sharkman.com">Sharkman</a>
 * <br>Email: <a href="mailto:526478642@qq.com">526478642@qq.com</a></p>
 *
 * @author yanpengyu
 * @version 1.0
 * @since 1.0
 */
public final class AutoData {
    private AutoData() {
    }

    /**
     * 利用程序造数据
     *
     * @param nodesCount 造的节点数量
     * @param maxChild   单级最大子节点
     * @param rootPid    根节点id， 可以为null
     * @return 列表数据
     */
    public static List<Treeable> makeRandomNodes(int nodesCount, int maxChild, String rootPid) {
        List<Treeable> nodes = new ArrayList<>(nodesCount + 1);
        // add root
        nodes.add(new TreeNode(String.valueOf(0), rootPid));
        Random random = new Random(System.currentTimeMillis());
        // traverse in interval
        // init start index border
        int startIndex = 0;
        while(nodes.size() < nodesCount) {
            int nextStartIndex = nodes.size();
            for (int i = startIndex; i < nextStartIndex; i++) {
                int childrenCount;
                int restSeat = nodesCount - nodes.size();
                if (restSeat < maxChild) {
                    childrenCount = restSeat;
                } else {
                    // random children count
                    childrenCount = random.nextInt(maxChild) + 1;
                }
                // add children nodes
                for (int j = 0; j < childrenCount; j++) {
                    nodes.add(new TreeNode(String.valueOf(nodes.size()), String.valueOf(i)));
                }
            }
            startIndex = nextStartIndex;
        }
        return nodes;
    }
}
