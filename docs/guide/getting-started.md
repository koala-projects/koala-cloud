# 快速开始

## 组件集成

1. 增加考拉提供的Maven仓库和依赖清单, 修改`pom.xml`:

```xml
<repositories>
  <repository>
    <id>koala</id>
    <name>koala</name>
      <url>https://raw.github.com/koala-projects/maven-repositories/public/</url>
      <!--<url>https://gitee.com/koala-projects/maven-repositories/raw/snapshot/</url>-->
  </repository>
</repositories>

<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>cn.koala.cloud</groupId>
      <artifactId>koala-cloud-dependencies</artifactId>
      <version>2023.0.0-SNAPSHOT</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

2. 选取自己需要的组件:

   | 组件名称                                                     | 组件说明                                                     |
   | ------------------------------------------------------------ | ------------------------------------------------------------ |
   | [koala-cloud-gateway-starter](../../koala-cloud-starters/koala-cloud-gateway-starter) | 快速集成 Spring Cloud Gateway, 提供 动态路由 / 接口鉴权 / 接口日志 等高级功能 |
   
3. 引入组件依赖, 以网关为例, 修改`pom.xml`:

```xml
<dependencies>
  <dependency>
    <groupId>cn.koala.cloud</groupId>
    <artifactId>koala-cloud-gateway-starter</artifactId>
  </dependency>
</dependencies>
```

## 初始化数据库

在[数据库脚本目录](../../db/migration)下选择项目数据库类型的数据库脚本, 执行即可
