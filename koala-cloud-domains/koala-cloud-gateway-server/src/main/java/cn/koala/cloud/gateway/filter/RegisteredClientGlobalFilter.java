package cn.koala.cloud.gateway.filter;

import cn.koala.cloud.gateway.model.RegisteredClient;
import cn.koala.cloud.gateway.repository.RegisteredClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 注册客户端全局过滤器
 *
 * <p>用于将注册客户端信息存入请求属性中
 *
 * @author Houtaroy
 */
@RequiredArgsConstructor
public class RegisteredClientGlobalFilter implements GlobalFilter, Ordered {

  private final RegisteredClientRepository repository;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String clientId = exchange.getAttribute(OAuth2ParameterNames.CLIENT_ID);
    if (StringUtils.hasText(clientId)) {
      return repository.findByClientId(clientId)
        .doOnNext(client -> exchange.getAttributes().put(RegisteredClient.class.getName(), client))
        .then(chain.filter(exchange));
    }
    return chain.filter(exchange);
  }

  @Override
  public int getOrder() {
    return 100;
  }
}
