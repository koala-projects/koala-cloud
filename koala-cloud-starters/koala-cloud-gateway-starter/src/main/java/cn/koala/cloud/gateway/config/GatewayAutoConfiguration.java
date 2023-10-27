package cn.koala.cloud.gateway.config;

import cn.koala.cloud.gateway.ApiAuthorizationRepository;
import cn.koala.cloud.gateway.ApiRepository;
import cn.koala.cloud.gateway.RouteRepository;
import cn.koala.cloud.gateway.filter.ApiAuthorizationGlobalFilter;
import cn.koala.cloud.gateway.filter.ApiGlobalFilter;
import cn.koala.cloud.gateway.filter.factory.ApiAuthorizationGatewayFilterFactory;
import cn.koala.cloud.gateway.filter.factory.ApiIpGatewayFilterFactory;
import cn.koala.cloud.gateway.route.DatabaseRouteDefinitionLocator;
import cn.koala.cloud.gateway.security.authorization.OAuth2ReactiveAuthorizationManager;
import cn.koala.cloud.gateway.task.RefreshRoutesTask;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * 网关自动配置类
 *
 * @author Houtaroy
 */
@Configuration
@EnableScheduling
@EnableWebFluxSecurity
@EnableR2dbcRepositories("cn.koala.cloud.gateway")
public class GatewayAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(name = "apiAuthorizationGatewayFilterFactory")
  public GatewayFilterFactory<ApiAuthorizationGatewayFilterFactory.Config> apiAuthorizationGatewayFilterFactory() {
    return new ApiAuthorizationGatewayFilterFactory();
  }

  @Bean
  @ConditionalOnMissingBean(name = "apiIpGatewayFilterFactory")
  public GatewayFilterFactory<ApiIpGatewayFilterFactory.Config> apiIpGatewayFilterFactory() {
    return new ApiIpGatewayFilterFactory();
  }

  @Bean
  @ConditionalOnMissingBean(name = "apiGlobalFilter")
  public GlobalFilter apiGlobalFilter(ApiRepository apiRepository) {
    return new ApiGlobalFilter(apiRepository);
  }

  @Bean
  @ConditionalOnMissingBean(name = "apiAuthorizationGlobalFilter")
  public GlobalFilter apiAuthorizationGlobalFilter(ApiAuthorizationRepository apiAuthorizationRepository) {
    return new ApiAuthorizationGlobalFilter(apiAuthorizationRepository);
  }

  @Bean
  @ConditionalOnMissingBean(name = "databaseRouteDefinitionLocator")
  public RouteDefinitionLocator databaseRouteDefinitionLocator(RouteRepository routeRepository) {
    return new DatabaseRouteDefinitionLocator(routeRepository);
  }

  @Bean
  @ConditionalOnMissingBean(name = "refreshRoutesTask")
  public RefreshRoutesTask refreshRoutesTask() {
    return new RefreshRoutesTask();
  }

  @Bean
  @ConditionalOnMissingBean(name = "gatewaySecurityWebFilterChain")
  public SecurityWebFilterChain gatewaySecurityWebFilterChain(ServerHttpSecurity http) {
    return http.csrf().disable()
      .authorizeExchange((exchange) ->
        exchange.pathMatchers("/oauth2/**").permitAll()
          .anyExchange().access(new OAuth2ReactiveAuthorizationManager())
      )
      .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt)
      .build();
  }
}
