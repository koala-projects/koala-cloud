package cn.koala.cloud.gateway.filter;

import cn.koala.cloud.gateway.model.Api;
import cn.koala.cloud.gateway.model.Resource;
import cn.koala.cloud.gateway.repository.ApiRepository;
import cn.koala.cloud.gateway.web.ApiRequestMatcher;
import cn.koala.cloud.gateway.web.SimpleApiRequestMatcher;
import cn.koala.persist.domain.YesNo;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
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
public class ApiAttributeGlobalFilter implements GlobalFilter, Ordered {

  private final ApiRepository repository;
  private final ApiRequestMatcher matcher;

  public ApiAttributeGlobalFilter(ApiRepository apiRepository) {
    this(apiRepository, new SimpleApiRequestMatcher());
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    Resource resource = exchange.getAttribute(Resource.class.getName());
    if (resource == null) {
      return chain.filter(exchange);
    }
    return repository.findByResourceIdAndIsEnabledAndIsDeletedOrderBySortIndex(
        resource.getId(),
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
    return FilterOrders.API_ATTRIBUTE;
  }
}
