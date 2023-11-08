package cn.koala.cloud.gateway.security.config;

import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 权限校验放开安全过滤器链处理器
 *
 * @author Houtaroy
 */
public class PermitAllSecurityFilterChainProcessor implements GatewayServerSecurityFilterChainProcessor {

  private final String[] patterns;

  public PermitAllSecurityFilterChainProcessor(String... patterns) {
    this.patterns = patterns;
  }

  public PermitAllSecurityFilterChainProcessor(List<String> patterns) {
    this.patterns = patterns.toArray(new String[0]);
  }

  @Override
  public void preBuild(ServerHttpSecurity http) throws Exception {
    if (ObjectUtils.isEmpty(this.patterns)) {
      return;
    }
    http.authorizeExchange().pathMatchers(this.patterns).permitAll();
  }

  @Override
  public void postBuild(ServerHttpSecurity http) throws Exception {

  }
}
