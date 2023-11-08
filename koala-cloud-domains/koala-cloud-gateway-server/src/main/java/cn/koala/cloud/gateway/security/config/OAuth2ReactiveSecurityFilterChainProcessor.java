package cn.koala.cloud.gateway.security.config;

import cn.koala.cloud.gateway.repository.ApiDocumentRepository;
import cn.koala.cloud.gateway.security.authorization.OAuth2ReactiveAuthorizationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.web.server.ServerHttpSecurity;

/**
 * OAuth2安全过滤器链处理器
 * <p>
 * 放开OAuth2接口, 增加自定义权限管理
 *
 * @author Houtaroy
 */
@RequiredArgsConstructor
public class OAuth2ReactiveSecurityFilterChainProcessor implements GatewayServerSecurityFilterChainProcessor {

  private final ApiDocumentRepository apiDocumentRepository;

  @Override
  public void preBuild(ServerHttpSecurity http) {
    http.csrf().disable()
      .authorizeExchange()
      .pathMatchers("/oauth2/**").permitAll()
      .anyExchange().access(new OAuth2ReactiveAuthorizationManager(apiDocumentRepository))
      .and()
      .oauth2ResourceServer().jwt();
  }

  @Override
  public void postBuild(ServerHttpSecurity http) {

  }
}
