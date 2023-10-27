package cn.koala.cloud.gateway.filter;

import cn.koala.cloud.gateway.Api;
import cn.koala.cloud.gateway.ApiRepository;
import cn.koala.persist.domain.YesNo;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
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

  private final PathPatternParser pathPatternParser = new PathPatternParser();
  private final ApiRepository apiRepository;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
    if (route == null || !StringUtils.hasText(route.getId())) {
      return chain.filter(exchange);
    }
    return apiRepository
      .findByResourceIdAndIsEnabledAndIsDeleted(route.getId(), YesNo.YES.getValue(), YesNo.NO.getValue())
      .filter(api -> isApi(exchange.getRequest(), api))
      .next()
      .doOnNext(api -> exchange.getAttributes().put(Api.class.getName(), api))
      .then(chain.filter(exchange));
  }

  private boolean isApi(ServerHttpRequest request, Api api) {
    if (request.getMethod().matches(api.getMethod())) {
      PathPattern pattern = pathPatternParser.parse(api.getPath());
      return pattern.matches(request.getPath());
    }
    return false;
  }

  @Override
  public int getOrder() {
    return 1000;
  }
}
