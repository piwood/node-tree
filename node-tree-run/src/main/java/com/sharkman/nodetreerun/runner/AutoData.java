package com.sharkman.nodetreerun.runner;

import com.sharkman.commons.tree.Treeable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <p> Description:AutoData</p>
 * <p> CreationTime: 2020/5/8 18:12
 * <br>Copyright: &copy;2020 <a href="http://www.thunisoft.com">Thunisoft</a>
 * <br>Email: <a href="mailto:yanpengyu@thunisoft.com">yanpengyu@thunisoft.com</a></p>
 *
 * @author yanpengyu
 * @since 1.0
 */
public final class AutoData {
    private AutoData() {
    }

    public static List<Treeable> makeRandomNodes(int nodesCount, int maxChild, String rootPid) {
        List<Treeable> nodes = new ArrayList<>(nodesCount);
        // add root
        nodes.add(new TreeNode(String.valueOf(0), rootPid));
        Random random = new Random(maxChild);
        // init start index border
        int startIndex = 0;
        // traverse in interval
        while(nodes.size() < nodesCount) {
            int endIndex = nodes.size();
            for (int i = startIndex; i < endIndex; i++) {
                // random children count
                int childrenCount = random.nextInt();
                // add children nodes
                for (int j = 0; j < childrenCount; j++) {
                    nodes.add(new TreeNode(String.valueOf(i + j), String.valueOf(i)));
                }
                // index offset
                startIndex += childrenCount;
            }
        }
        return nodes;
    }
}
