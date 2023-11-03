package cn.koala.cloud.gateway.config;

import cn.koala.cloud.gateway.filter.ApiAuthorizationGlobalFilter;
import cn.koala.cloud.gateway.filter.ApiGlobalFilter;
import cn.koala.cloud.gateway.filter.ApiRequestLogGlobalFilter;
import cn.koala.cloud.gateway.filter.ApiResponseLogGlobalFilter;
import cn.koala.cloud.gateway.filter.CacheRequestBodyStringGlobalFilter;
import cn.koala.cloud.gateway.filter.DecryptRequestGlobalFilter;
import cn.koala.cloud.gateway.filter.RegisteredClientGlobalFilter;
import cn.koala.cloud.gateway.filter.factory.ApiAuthorizationGatewayFilterFactory;
import cn.koala.cloud.gateway.filter.factory.ApiIpGatewayFilterFactory;
import cn.koala.cloud.gateway.model.converter.StringClientSettingsConverter;
import cn.koala.cloud.gateway.repository.ApiAuthorizationRepository;
import cn.koala.cloud.gateway.repository.ApiExceptionLogRepository;
import cn.koala.cloud.gateway.repository.ApiRepository;
import cn.koala.cloud.gateway.repository.ApiRequestLogRepository;
import cn.koala.cloud.gateway.repository.ApiResponseLogRepository;
import cn.koala.cloud.gateway.repository.RegisteredClientRepository;
import cn.koala.cloud.gateway.repository.ResourceRepository;
import cn.koala.cloud.gateway.repository.RouteRepository;
import cn.koala.cloud.gateway.route.DatabaseRouteDefinitionLocator;
import cn.koala.cloud.gateway.security.authorization.OAuth2ReactiveAuthorizationManager;
import cn.koala.cloud.gateway.task.RefreshRoutesTask;
import cn.koala.cloud.gateway.web.ApiExceptionLogHandler;
import cn.koala.cloud.gateway.web.SimpleApiRequestMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.MySqlDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.WebExceptionHandler;

/**
 * 网关自动配置类
 *
 * @author Houtaroy
 */
@Configuration
@EnableScheduling
@EnableWebFluxSecurity
@EnableR2dbcRepositories("cn.koala.cloud.gateway")
@EnableConfigurationProperties(GatewayProperties.class)
public class GatewayAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(name = "cacheRequestBodyStringGlobalFilter")
  public GlobalFilter cacheRequestBodyStringGlobalFilter() {
    return new CacheRequestBodyStringGlobalFilter();
  }

  @Bean
  @ConditionalOnMissingBean(name = "registeredClientGlobalFilter")
  public GlobalFilter registeredClientGlobalFilter(RegisteredClientRepository registeredClientRepository) {
    return new RegisteredClientGlobalFilter(registeredClientRepository);
  }

  @Bean
  @ConditionalOnMissingBean(name = "apiGlobalFilter")
  public GlobalFilter apiGlobalFilter(ApiRepository apiRepository) {
    return new ApiGlobalFilter(apiRepository, new SimpleApiRequestMatcher());
  }

  @Bean
  @ConditionalOnMissingBean(name = "apiAuthorizationGlobalFilter")
  public GlobalFilter apiAuthorizationGlobalFilter(ApiAuthorizationRepository apiAuthorizationRepository) {
    return new ApiAuthorizationGlobalFilter(apiAuthorizationRepository);
  }

  @Bean
  @ConditionalOnMissingBean(name = "decryptRequestGlobalFilter")
  public GlobalFilter decryptRequestGlobalFilter(ObjectMapper objectMapper) {
    return new DecryptRequestGlobalFilter(objectMapper);
  }

  @Bean
  @ConditionalOnMissingBean(name = "apiResponseLogGlobalFilter")
  public GlobalFilter apiResponseLogGlobalFilter(ApiResponseLogRepository apiResponseLogRepository,
                                                 ObjectMapper objectMapper) {

    return new ApiResponseLogGlobalFilter(apiResponseLogRepository, objectMapper);
  }

  @Bean
  @ConditionalOnMissingBean(name = "apiRequestLogGlobalFilter")
  @ConditionalOnProperty(prefix = "koala.cloud.gateway", name = "logging", havingValue = "true", matchIfMissing = true)
  public GlobalFilter apiRequestLogGlobalFilter(
    ApiRequestLogRepository apiRequestLogRepository, RegisteredClientRepository clientRepository,
    RouteRepository routeRepository, ResourceRepository resourceRepository, ObjectMapper objectMapper) {

    return new ApiRequestLogGlobalFilter(apiRequestLogRepository, clientRepository, routeRepository,
      resourceRepository, objectMapper);
  }

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

  @Order(Ordered.HIGHEST_PRECEDENCE)
  @Bean
  @ConditionalOnMissingBean(name = "apiExceptionLogHandler")
  public WebExceptionHandler apiExceptionLogHandler(ApiExceptionLogRepository apiExceptionLogRepository) {
    return new ApiExceptionLogHandler(apiExceptionLogRepository);
  }

  @Bean
  @ConditionalOnMissingBean(name = "databaseRouteDefinitionLocator")
  @ConditionalOnProperty(prefix = "koala.cloud.gateway", name = "dynamic-routing", havingValue = "true")
  public RouteDefinitionLocator databaseRouteDefinitionLocator(RouteRepository routeRepository) {
    return new DatabaseRouteDefinitionLocator(routeRepository);
  }

  @Bean
  @ConditionalOnMissingBean(name = "refreshRoutesTask")
  @ConditionalOnProperty(prefix = "koala.cloud.gateway", name = "dynamic-routing", havingValue = "true")
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

  @Bean
  public R2dbcCustomConversions r2dbcCustomConversions(ObjectMapper objectMapper) {
    return R2dbcCustomConversions.of(
      MySqlDialect.INSTANCE,
      new StringClientSettingsConverter(objectMapper)
    );
  }
}
