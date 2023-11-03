package cn.koala.cloud.gateway.filter;

import cn.koala.cloud.gateway.model.Api;
import cn.koala.cloud.gateway.model.ApiAuthorization;
import cn.koala.cloud.gateway.repository.ApiAuthorizationRepository;
import cn.koala.persist.domain.YesNo;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * 接口授权信息全局过滤器
 * <p>
 * 将接口授权信息添加到exchange的属性中
 *
 * @author Houtaroy
 */
@RequiredArgsConstructor
public class ApiAuthorizationAttributeGlobalFilter implements GlobalFilter, Ordered {

  private final ApiAuthorizationRepository apiAuthorizationRepository;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String clientId = exchange.getAttribute(OAuth2ParameterNames.CLIENT_ID);
    Api api = exchange.getAttribute(Api.class.getName());
    if (!StringUtils.hasText(clientId) || api == null || api.getId() == null) {
      return chain.filter(exchange);
    }
    return apiAuthorizationRepository
      .findByClientIdAndApiIdAndExpiresAtAfterAndIsDeleted(clientId, api.getId(), LocalDate.now(), YesNo.NO.getValue())
      .doOnNext(authorizations -> exchange.getAttributes().put(ApiAuthorization.class.getName(), authorizations))
      .then(chain.filter(exchange));
  }

  @Override
  public int getOrder() {
    return FilterOrders.API_AUTHORIZATION_ATTRIBUTE;
  }
}
