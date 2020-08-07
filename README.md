# 迭代方式生成树形节点

#### 介绍
工具类，将id，pid 形式的数据生成前端控件常用的 parent-> children 结构。

#### 软件架构
jdk8.0+maven

#### 使用说明
1. 只需要引入 node-tree-core 包
```xml
<dependency>
  <groupId>com.sharkman</groupId>
  <artifactId>node-tree-core</artifactId>
  <version>1.1</version>
</dependency>
```
2. 创建树节点类，并实现 Treeable 接口
3. 调用 TreeUtil 对应方法实现方法调用

#### 参与贡献

1.  Fork 本仓库
2.  新建 feature_xxx 分支
3.  提交代码
4.  新建 Pull Request