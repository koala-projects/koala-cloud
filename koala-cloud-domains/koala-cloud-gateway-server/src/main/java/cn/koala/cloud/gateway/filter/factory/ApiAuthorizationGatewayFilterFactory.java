package cn.koala.cloud.gateway.filter.factory;

import cn.koala.cloud.gateway.filter.FilterOrders;
import cn.koala.cloud.gateway.model.Api;
import cn.koala.cloud.gateway.model.ApiAuthorization;
import cn.koala.cloud.gateway.repository.OAuth2AuthorizationRepository;
import cn.koala.cloud.gateway.util.GatewayUtils;
import cn.koala.persist.domain.YesNo;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;

/**
 * 接口授权校验网关过滤器工厂
 *
 * @author Houtaroy
 */
public class ApiAuthorizationGatewayFilterFactory extends AbstractGatewayFilterFactory<ApiAuthorizationGatewayFilterFactory.Config> {

  private final OAuth2AuthorizationRepository oauth2AuthorizationRepository;

  public ApiAuthorizationGatewayFilterFactory(OAuth2AuthorizationRepository oauth2AuthorizationRepository) {
    super(Config.class);
    this.oauth2AuthorizationRepository = oauth2AuthorizationRepository;
  }

  @Override
  public GatewayFilter apply(Config config) {
    return new OrderedGatewayFilter(
      (exchange, chain) -> {

        Api api = exchange.getAttribute(Api.class.getName());
        if (api == null) {
          return GatewayUtils.setResponse(exchange, HttpStatus.NOT_FOUND, "接口不存在");
        }
        if (api.getIsPermissible() == YesNo.YES.getValue()) {
          return chain.filter(exchange);
        }

        String token = exchange.getAttribute(OAuth2ParameterNames.TOKEN);
        if (!StringUtils.hasText(token)) {
          return GatewayUtils.setResponse(exchange, HttpStatus.UNAUTHORIZED, "未授权");
        }

        ApiAuthorization authorization = exchange.getAttribute(ApiAuthorization.class.getName());
        if (authorization == null) {
          return GatewayUtils.setResponse(exchange, HttpStatus.NOT_FOUND, "接口不存在");
        }

        return oauth2AuthorizationRepository.findByAccessTokenValue(token)
          .hasElement()
          .flatMap(hasElement ->
            hasElement
              ? chain.filter(exchange)
              : GatewayUtils.setResponse(exchange, HttpStatus.UNAUTHORIZED, "未授权")
          );
      },
      FilterOrders.API_AUTHORIZATION
    );
  }

  public static class Config {

  }
}
