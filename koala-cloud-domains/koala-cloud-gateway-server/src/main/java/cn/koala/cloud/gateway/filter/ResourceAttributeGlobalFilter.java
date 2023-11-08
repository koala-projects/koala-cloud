package cn.koala.cloud.gateway.filter;

import cn.koala.cloud.gateway.model.Resource;
import cn.koala.cloud.gateway.repository.ResourceRepository;
import cn.koala.cloud.gateway.util.RouteUtils;
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
public class ResourceAttributeGlobalFilter implements GlobalFilter, Ordered {

  private final ResourceRepository repository;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
    if (route == null || !StringUtils.hasText(route.getId())) {
      return chain.filter(exchange);
    }
    return repository.findById(RouteUtils.obtainResourceId(route))
      .doOnNext(resource -> exchange.getAttributes().put(Resource.class.getName(), resource))
      .then(chain.filter(exchange));
  }

  @Override
  public int getOrder() {
    return FilterOrders.RESOURCE_ATTRIBUTE;
  }
}
