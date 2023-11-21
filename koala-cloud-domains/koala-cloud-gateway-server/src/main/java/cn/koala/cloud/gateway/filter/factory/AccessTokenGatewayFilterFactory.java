package cn.koala.cloud.gateway.filter.factory;

import cn.koala.cloud.gateway.filter.FilterOrders;
import cn.koala.cloud.gateway.repository.OAuth2AuthorizationRepository;
import cn.koala.cloud.gateway.util.GatewayUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;

/**
 * AccessToken校验网关过滤器工厂
 *
 * @author Houtaroy
 */
@Deprecated
public class AccessTokenGatewayFilterFactory extends AbstractGatewayFilterFactory<AccessTokenGatewayFilterFactory.Config> {

  private final OAuth2AuthorizationRepository oauth2AuthorizationRepository;

  public AccessTokenGatewayFilterFactory(OAuth2AuthorizationRepository oauth2AuthorizationRepository) {
    super(Config.class);
    this.oauth2AuthorizationRepository = oauth2AuthorizationRepository;
  }

  @Override
  public GatewayFilter apply(Config config) {
    return new OrderedGatewayFilter(
      (exchange, chain) -> {

        String token = exchange.getAttribute(OAuth2ParameterNames.TOKEN);
        if (!StringUtils.hasText(token)) {
          return GatewayUtils.setResponse(exchange, HttpStatus.UNAUTHORIZED, "未授权");
        }

        return oauth2AuthorizationRepository.findByAccessTokenValue(token)
          .hasElement()
          .flatMap(hasElement ->
            hasElement
              ? chain.filter(exchange)
              : GatewayUtils.setResponse(exchange, HttpStatus.UNAUTHORIZED, "未授权")
          );
      },
      FilterOrders.ACCESS_TOKEN
    );
  }

  public static class Config {

  }
}
