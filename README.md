# 迭代方式生成树形节点

#### 介绍
工具类，将id，pid 形式的数据生成前端控件常用的 parent-> children 结构。

#### 软件架构
jdk8.0+maven

#### 使用说明
##### 直接使用工具类
1. 只需要引入 node-tree-core 包
```xml
<dependency>
  <groupId>com.sharkman</groupId>
  <artifactId>node-tree-core</artifactId>
  <version>1.1.4</version>
</dependency>
```
2. 创建树节点类，并实现 Treeable 接口
```java
// 示例使用 lombok 生成 getter and setter
@Getter
@Setter
public final class TreeNode implements Treeable {
    private String id;
    private String pId;
    private List<Treeable> children;
    // 可添加其他属性以及方法,如
    private String nodeType;
}
```
3. 调用 TreeUtil 对应方法实现方法调用
  - 场景1，已知根节点的父id，构造树。 父 id 可以为 null
  ```java
  // 若根节点唯一 
  Treeable root = TreeUtil.buildTreeOfRootPId(nodes, rootPid);
  // 若根节点不唯一
  List<Treeable> root = TreeUtil.buildTreeOfRootPIdForList(nodes, rootPid);
  ```
  - 场景2，已知根节点的id，构造树。
  ```java
  // 若根节点唯一 
  Treeable root = TreeUtil.buildTreeOfRootId(nodes, rootId);
  // 若根节点不唯一
  List<Treeable> root = TreeUtil.buildTreeOfRootIdForList(nodes, rootId);
  ```
  - 场景3，已知符合根节点的条件，可以使用 lambda 表达式找到根节点，构造树。
  ```java
  // 若根节点唯一 
  Predicate<Treeable> predicate = vo -> Objects.equals(vo.getPId(), "root") && OrganEnum.isCorp(vo.getNodeType());
  Treeable root = TreeUtil.buildTree(nodes, predicate);
  // 若根节点不唯一
  List<Treeable> root = TreeUtil.buildTreeForList(nodes, predicate);
  ```
##### 基于 NodeTree 注解返回值转化为属性结构
1. 引入 node-tree-starter 包
```xml
<dependency>
  <groupId>com.sharkman</groupId>
  <artifactId>node-tree-starter</artifactId>
  <version>1.1.4</version>
</dependency>
```

2. 创建树节点类，使用注解标注 id ，pid ，children 属性

```java
// 示例使用 lombok 生成 getter and setter
@Getter
@Setter
public final class TreeNode {
    @NodeID
    private String id;
    @NodePID
    private String pId;
    @NodeChildren
    private List<TreeNode> children;
    // 其他属性和方法...
}
```
3. 在需要的地方写注解
- 场景一：固定根节点id 或 pid 为固定值
> 注意： 如果 id 和 pid 都填值，则以 id 为准。
```java
// 根节点 pid 为固定值 0 
@NodeTree(pid = "0")
public List<TreeNode> findAllNodes(int nodesCount) {
    int maxChild = 2;
    return AutoData.makeRandomNodes(nodesCount, maxChild, "0");
}

// 根节点 id 为固定值 0
@NodeTree(id = "0")
public List<TreeNode> findAllNodes(int nodesCount) {
    int maxChild = 2;
    return AutoData.makeRandomNodes(nodesCount, maxChild, "0");
}

// 根节点 pid 为 null 
@NodeTree(isPidNull = true)
public List<TreeNode> findAllNodes(int nodesCount) {
    int maxChild = 2;
    return AutoData.makeRandomNodes(nodesCount, maxChild, "0");
}
```

- 场景二：动态传入根节点id 或 pid，且id 和 pid 为方法参数
```java
// 通过 Id 获取
@NodeTree
public List<TreeNode> findAllNodesByCorpId(@RootID String corpId) {
    return corpRepository.findCorpTreeByCorpId(corpId);
}

// 通过 父Id 获取
@NodeTree
public List<TreeNode> findAllNodesByPCorpId(@RootPID String corpId) {
    return corpRepository.findCorpTreeByPCorpId(corpId);
}
```
- 场景三：动态传入根节点id 或 pid，且id 和 pid 为方法参数的对象属性


```java
// 参数对象
@Getter
@Setter
public final class SearchParam {
    @RootID 
    private String corpId; // 此处增加注解，获取对象内部属性值作为 root id
    private Date startDate;
    private Date endDate;
    // 其他属性和方法...
}
// 通过 Id 获取
@NodeTree
public List<TreeNode> findAllNodesByCorpId(SearchParam param) {
    return corpRepository.findCorpTreeByCorpId(param.getCorp());
}


// 参数对象
@Getter
@Setter
public final class SearchParam {
    @RootPID 
    private String corpId; // 此处增加注解，获取对象内部属性值作为 root pid
    private Date startDate;
    private Date endDate;
    // 其他属性和方法...
}
// 通过 父Id 获取
@NodeTree
public List<TreeNode> findAllNodesByPCorpId(SearchParam param) {
    return corpRepository.findCorpTreeByPCorpId(param.getCorp());
}
```
##### 性能表现
> 测试代码已添加， 见子项目 node-tree-run AutoDataTest测试类
TODO 待补充
#### 参与贡献
1.  Fork 本仓库
2.  新建 feature_xxx 分支
3.  提交代码
4.  新建 Pull Request