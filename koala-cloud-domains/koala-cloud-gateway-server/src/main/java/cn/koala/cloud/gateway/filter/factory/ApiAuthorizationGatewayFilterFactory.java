package cn.koala.cloud.gateway.filter.factory;

import cn.koala.cloud.gateway.filter.FilterOrders;
import cn.koala.cloud.gateway.model.Api;
import cn.koala.cloud.gateway.model.ApiAuthorization;
import cn.koala.persist.domain.YesNo;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;

/**
 * 接口授权校验网关过滤器工厂
 *
 * @author Houtaroy
 */
public class ApiAuthorizationGatewayFilterFactory extends AbstractGatewayFilterFactory<ApiAuthorizationGatewayFilterFactory.Config> {

  public ApiAuthorizationGatewayFilterFactory() {
    super(Config.class);
  }

  @Override
  public GatewayFilter apply(Config config) {
    return new OrderedGatewayFilter((exchange, chain) -> {
      Api api = exchange.getAttribute(Api.class.getName());
      ApiAuthorization authorization = exchange.getAttribute(ApiAuthorization.class.getName());
      if (api == null) {
        exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
        return exchange.getResponse().setComplete();
      }
      if (api.getIsPermissible() == YesNo.NO.getValue() && authorization == null) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
      }
      return chain.filter(exchange);
    }, FilterOrders.API_AUTHORIZATION);
  }

  public static class Config {

  }
}
