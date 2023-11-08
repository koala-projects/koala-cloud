package cn.koala.cloud.gateway.security.config;

import org.springframework.security.config.web.server.ServerHttpSecurity;

/**
 * 网关服务安全过滤链处理器
 *
 * @author Houtaroy
 */
public interface GatewayServerSecurityFilterChainProcessor {

  void preBuild(ServerHttpSecurity http) throws Exception;

  void postBuild(ServerHttpSecurity http) throws Exception;
}
