package cn.koala.cloud.gateway.filter;

import cn.koala.cloud.gateway.model.Api;
import cn.koala.cloud.gateway.repository.ApiRepository;
import cn.koala.cloud.gateway.util.RouteUtils;
import cn.koala.cloud.gateway.web.ApiRequestMatcher;
import cn.koala.persist.domain.YesNo;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 接口信息全局过滤器
 * <p>
 * 将接口信息添加到exchange的属性中
 *
 * @author Houtaroy
 */
@RequiredArgsConstructor
public class ApiGlobalFilter implements GlobalFilter, Ordered {

  private final ApiRepository repository;
  private final ApiRequestMatcher matcher;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
    if (route == null || !StringUtils.hasText(route.getId())) {
      return chain.filter(exchange);
    }
    return repository.findByResourceIdAndIsEnabledAndIsDeleted(
        RouteUtils.obtainResourceId(route),
        YesNo.YES.getValue(),
        YesNo.NO.getValue()
      )
      .filter(api -> matcher.matches(api, exchange.getRequest()))
      .next()
      .doOnNext(api -> exchange.getAttributes().put(Api.class.getName(), api))
      .then(chain.filter(exchange));
  }

  @Override
  public int getOrder() {
    return 200;
  }
}
