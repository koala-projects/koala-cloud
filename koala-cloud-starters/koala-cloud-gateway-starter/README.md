# Koala Cloud Gateway Starter

考拉网关启动模块

基于 [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway) 开发, 提供 动态路由 / 接口鉴权 / 接口日志 等高级功能

## 快速开始

### 数据库

请先参照[快速开始](../../docs/guide/getting-started.md#初始化数据库)初始化数据库

### 网关配置

```yaml
koala:
  cloud:
    gateway:
      # 开启动态路由
      dynamic-routing: true
      # 关闭网关日志
      logging: false
```

## 进阶

### 网关过滤器

| 过滤器名称                             | 过滤器功能                           | 过滤器排序                   |
| -------------------------------------- | ------------------------------------ | ---------------------------- |
| `ApiLogPreparationGlobalFilter`        | 为记录接口日志做准备, 包装请求和响应 | `Ordered.HIGHEST_PRECEDENCE` |
| `ApiGlobalFilter`                      | 在上下文中增加接口信息               | 1000                         |
| `ApiAuthorizationGlobalFilter`         | 在上下文中增加接口授权信息           | 1100                         |
| `ApiRequestLogGlobalFilter`            | 记录接口请求日志                     | 1200                         |
| `ApiAuthorizationGatewayFilterFactory` | 校验接口授权信息                     | 2000                         |
| `ApiIpGatewayFilterFactory`            | 校验接口IP信息                       | 2100                         |
