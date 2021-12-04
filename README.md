# 迭代方式生成树形节点

#### 介绍
将id，pid 形式的数据生成 parent-> children 树形结构。

#### 软件架构
jdk8.0+maven3  
spring-boot 2.2.6.RELEASE

#### 使用说明
##### 基于 NodeTree 注解返回值转化为属性结构
1. 引入 node-tree-spring-boot-starter 包

  ```xml

<dependency>
  <groupId>com.sharkman</groupId>
  <artifactId>node-tree-spring-boot-starter</artifactId>
  <version>1.1.8</version>
</dependency>
  ```

2. 创建树节点类
  树节点对象既支持使用注解标注 id ，pid ，children 属性  
  也支持不使用注解标注，实现 Treeable 接口  
  程序会自动识别使用哪种形式。

  ```java
  // 注解版
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

  // 实现接口版
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
3. 在返回值需要转化为树形式的方法上加上 @NodeTree 注解
> 前提，方法的返回值必须是 List<节点对象> , 节点对象可使用注解标注或实现接口。
  - 场景一：固定根节点id 或 pid 为固定值
  > 注意： 如果 id 和 pid 都填值，则以 id 为准。
  ```java
  // 根节点 pid 为固定值 0 
  @NodeTree(pid = "0")
  public List<TreeNode> findAllNodes(int nodesCount) {
      int maxChild = 2;
      return AutoData.makeRandomNodes(nodesCount, maxChild, "0");
  }
  
  // 注意，如果 TreeNode 实现了Treeable 接口，那么返回值写接口或者实现类均可，即
  // 返回值是 List<TreeNode> List<Treeable> 均可，下不赘述 
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
  // 通过 Id 获取, 方法参数个数不限，在对应参数前加注解即可
  @NodeTree
  public List<TreeNode> findAllNodesByCorpId(@RootID String corpId) {
      return corpRepository.findCorpTreeByCorpId(corpId);
  }
  
  // 通过 父Id 获取，方法参数个数不限，在对应参数前加注解即可
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
  // 通过 Id 获取，方法参数个数不限，在对应参数前加注解即可
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
  // 通过 父Id 获取，方法参数个数不限，在对应参数前加注解即可
  @NodeTree
  public List<TreeNode> findAllNodesByPCorpId(SearchParam param) {
      return corpRepository.findCorpTreeByPCorpId(param.getCorp());
  }
  ```
##### 直接使用工具类
1. 只需要引入 node-tree-core 包
  ```xml
  <dependency>
    <groupId>com.sharkman</groupId>
    <artifactId>node-tree-core</artifactId>
    <version>1.1.5</version>
  </dependency>
  ```
2. 创建树节点类，对象同上
3. 调用 TreeUtil 或 TreeUtilForAnnotation 对应方法实现功能
  - 场景1，已知根节点的父id，构造树。 父 id 可以为 null
  ```java
  // 接口形式
  // 若根节点唯一 
  Treeable root = TreeUtil.buildTreeOfRootPId(nodes, rootPid);
  // 若根节点不唯一
  List<Treeable> root = TreeUtil.buildTreeOfRootPIdForList(nodes, rootPid);
  // 注解形式
  // 若根节点唯一 
  TreeNode root = TreeUtilForAnnotation.buildTreeOfRootPId(nodes, rootPid);
  // 若根节点不唯一
  List<TreeNode> root = TreeUtilForAnnotation.buildTreeOfRootPIdForList(nodes, rootPid);
  ```
  - 场景2，已知根节点的id，构造树。
  ```java
  // 若根节点唯一 
  Treeable root = TreeUtil.buildTreeOfRootId(nodes, rootId);
  // 若根节点不唯一
  List<Treeable> root = TreeUtil.buildTreeOfRootIdForList(nodes, rootId);
  // 注解形式
  // 若根节点唯一 
  TreeNode root = TreeUtilForAnnotation.buildTreeOfRootId(nodes, rootId);
  // 若根节点不唯一
  List<TreeNode> root = TreeUtilForAnnotation.buildTreeOfRootIdForList(nodes, rootId);
  ```
  - 场景3，已知符合根节点的条件，可以使用 lambda 表达式找到根节点，构造树。
  ```java
  // 若根节点唯一 
  Predicate<Treeable> predicate = vo -> Objects.equals(vo.getPId(), "root") && OrganEnum.isCorp(vo.getNodeType());
  Treeable root = TreeUtil.buildTree(nodes, predicate);
  // 若根节点不唯一
  List<Treeable> root = TreeUtil.buildTreeForList(nodes, predicate);
  // 注解形式
  TreeNode root = TreeUtilForAnnotation.buildTree(nodes, predicate);
  // 若根节点不唯一
  List<TreeNode> root = TreeUtilForAnnotation.buildTreeForList(nodes, predicate);
  ```
  - 场景4，已知叶子节点，反向找出谱系内父节点，构造树。（此方法从项目中使用，未进行通用性优化。暂时不支持注解版）
  
  > 说明，常用的场景是数据权限进行组织机构过滤，权限只配置人员，但是需要展示有权限的组织树。
  > 另外，此方法支持取子树，比如共4级节点，通过4级节点逆向生成树，根节点传入3级id ，则根从传入的3级开始。

  ```java
  // 若根节点唯一 
// 起始构造的子节点集合
  List<String> ids=xxxx;
        // 最终取出的根节
        String rootId="root";
        List<Treeable> roots=TreeUtil.constructTreeForSpecifyNode(nodes,ids,rootId);
  ```

##### 性能表现

> 测试代码已添加， 见子项目 node-tree-run AutoDataTest测试类 TODO 待补充

#### 参与贡献

1. Fork 本仓库
2. 新建 feature_xxx 分支
3. 提交代码
4. 新建 Pull Request

#### 特别感谢 JetBrains 免费的开源授权
![JetBrains Logo (Main) logo](https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.png)