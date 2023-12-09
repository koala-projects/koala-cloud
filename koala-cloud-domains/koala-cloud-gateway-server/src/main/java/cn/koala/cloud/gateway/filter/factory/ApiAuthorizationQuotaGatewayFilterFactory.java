package cn.koala.cloud.gateway.filter.factory;

import cn.koala.cloud.gateway.filter.FilterOrders;
import cn.koala.cloud.gateway.model.ApiAuthorization;
import cn.koala.cloud.gateway.repository.ApiRequestLogRepository;
import cn.koala.cloud.gateway.util.GatewayUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;

import java.util.Objects;

/**
 * 接口授权配额校验网关过滤器工厂
 *
 * @author Houtaroy
 */
public class ApiAuthorizationQuotaGatewayFilterFactory extends AbstractGatewayFilterFactory<ApiAuthorizationQuotaGatewayFilterFactory.Config> {

  private static final int INFINITE = -1;
  private final ApiRequestLogRepository apiRequestLogRepository;

  public ApiAuthorizationQuotaGatewayFilterFactory(ApiRequestLogRepository apiRequestLogRepository) {
    super(Config.class);
    this.apiRequestLogRepository = apiRequestLogRepository;
  }

  @Override
  public GatewayFilter apply(Config config) {
    return new OrderedGatewayFilter((exchange, chain) -> {
      ApiAuthorization authorization = exchange.getAttribute(ApiAuthorization.class.getName());
      if (authorization == null || authorization.getQuota() == INFINITE) {
        return chain.filter(exchange);
      }
      return apiRequestLogRepository.countByClientIdAndApiIdAndRequestTimeAfter(
        authorization.getClientId(),
        authorization.getApiId(),
        Objects.requireNonNullElse(authorization.getLastModifiedTime(), authorization.getCreatedTime())
      ).flatMap(count ->
        count > authorization.getQuota()
          ? GatewayUtils.setResponse(exchange, HttpStatus.TOO_MANY_REQUESTS, "调用超过接口授权配额限制")
          : chain.filter(exchange)
      );
    }, FilterOrders.API_AUTHORIZATION_QUOTA);
  }

  public static class Config {

  }
}
