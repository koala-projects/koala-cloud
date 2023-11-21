package cn.koala.cloud.gateway.config;

import cn.koala.cloud.gateway.filter.ApiAttributeGlobalFilter;
import cn.koala.cloud.gateway.filter.ApiAuthorizationAttributeGlobalFilter;
import cn.koala.cloud.gateway.filter.ApiRequestLogGlobalFilter;
import cn.koala.cloud.gateway.filter.ApiResponseLogGlobalFilter;
import cn.koala.cloud.gateway.filter.CacheRequestBodyStringGlobalFilter;
import cn.koala.cloud.gateway.filter.DecryptRequestGlobalFilter;
import cn.koala.cloud.gateway.filter.EncryptResponseGlobalFilter;
import cn.koala.cloud.gateway.filter.RegisteredAttributeClientGlobalFilter;
import cn.koala.cloud.gateway.filter.ResourceAttributeGlobalFilter;
import cn.koala.cloud.gateway.filter.factory.ApiAuthorizationGatewayFilterFactory;
import cn.koala.cloud.gateway.filter.factory.ApiAuthorizationIpGatewayFilterFactory;
import cn.koala.cloud.gateway.filter.factory.ApiAuthorizationQuotaGatewayFilterFactory;
import cn.koala.cloud.gateway.model.converter.StringClientSettingsConverter;
import cn.koala.cloud.gateway.repository.ApiAuthorizationRepository;
import cn.koala.cloud.gateway.repository.ApiDocumentRepository;
import cn.koala.cloud.gateway.repository.ApiExceptionLogRepository;
import cn.koala.cloud.gateway.repository.ApiRepository;
import cn.koala.cloud.gateway.repository.ApiRequestLogRepository;
import cn.koala.cloud.gateway.repository.ApiResponseLogRepository;
import cn.koala.cloud.gateway.repository.OAuth2AuthorizationRepository;
import cn.koala.cloud.gateway.repository.RegisteredClientRepository;
import cn.koala.cloud.gateway.repository.ResourceRepository;
import cn.koala.cloud.gateway.repository.RouteRepository;
import cn.koala.cloud.gateway.route.DatabaseRouteDefinitionLocator;
import cn.koala.cloud.gateway.security.config.GatewayServerSecurityFilterChainProcessor;
import cn.koala.cloud.gateway.security.config.OAuth2ReactiveSecurityFilterChainProcessor;
import cn.koala.cloud.gateway.security.config.PermitAllSecurityFilterChainProcessor;
import cn.koala.cloud.gateway.security.config.SecurityFilterChainProcessorOrders;
import cn.koala.cloud.gateway.springdoc.SwaggerConfigApi;
import cn.koala.cloud.gateway.task.RefreshRoutesTask;
import cn.koala.cloud.gateway.web.ApiExceptionLogHandler;
import cn.koala.cloud.gateway.web.SimpleApiRequestMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.webflux.ui.SwaggerConfigResource;
import org.springframework.beans.factory.ObjectProvider;
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
import org.springframework.util.StringUtils;
import org.springframework.web.server.WebExceptionHandler;

import java.util.List;

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
  @ConditionalOnMissingBean(name = "registeredAttributeClientGlobalFilter")
  public GlobalFilter registeredAttributeClientGlobalFilter(RegisteredClientRepository registeredClientRepository) {
    return new RegisteredAttributeClientGlobalFilter(registeredClientRepository);
  }

  @Bean
  @ConditionalOnMissingBean(name = "resourceAttributeGlobalFilter")
  public GlobalFilter resourceAttributeGlobalFilter(ResourceRepository resourceRepository) {
    return new ResourceAttributeGlobalFilter(resourceRepository);
  }

  @Bean
  @ConditionalOnMissingBean(name = "apiAttributeGlobalFilter")
  public GlobalFilter apiAttributeGlobalFilter(ApiRepository apiRepository) {
    return new ApiAttributeGlobalFilter(apiRepository, new SimpleApiRequestMatcher());
  }

  @Bean
  @ConditionalOnMissingBean(name = "apiAuthorizationAttributeGlobalFilter")
  public GlobalFilter apiAuthorizationAttributeGlobalFilter(ApiAuthorizationRepository apiAuthorizationRepository) {
    return new ApiAuthorizationAttributeGlobalFilter(apiAuthorizationRepository);
  }

  @Bean
  @ConditionalOnMissingBean(name = "apiRequestLogGlobalFilter")
  @ConditionalOnProperty(prefix = "koala.cloud.gateway", name = "logging", havingValue = "true", matchIfMissing = true)
  public GlobalFilter apiRequestLogGlobalFilter(ApiRequestLogRepository apiRequestLogRepository,
                                                ObjectMapper objectMapper) {

    return new ApiRequestLogGlobalFilter(apiRequestLogRepository, objectMapper);
  }

  @Bean
  @ConditionalOnMissingBean(name = "decryptRequestGlobalFilter")
  public GlobalFilter decryptRequestGlobalFilter(ObjectMapper objectMapper) {
    return new DecryptRequestGlobalFilter(objectMapper);
  }

  @Bean
  @ConditionalOnMissingBean(name = "encryptResponseGlobalFilter")
  public GlobalFilter encryptResponseGlobalFilter(ObjectMapper objectMapper) {
    return new EncryptResponseGlobalFilter(objectMapper);
  }

  @Bean
  @ConditionalOnMissingBean(name = "apiResponseLogGlobalFilter")
  public GlobalFilter apiResponseLogGlobalFilter(ApiResponseLogRepository apiResponseLogRepository,
                                                 ObjectMapper objectMapper) {

    return new ApiResponseLogGlobalFilter(apiResponseLogRepository, objectMapper);
  }

  @Bean
  @ConditionalOnMissingBean(name = "apiAuthorizationGatewayFilterFactory")
  public GatewayFilterFactory<ApiAuthorizationGatewayFilterFactory.Config> apiAuthorizationGatewayFilterFactory(
    OAuth2AuthorizationRepository oauth2AuthorizationRepository) {

    return new ApiAuthorizationGatewayFilterFactory(oauth2AuthorizationRepository);
  }

  @Bean
  @ConditionalOnMissingBean(name = "apiAuthorizationIpGatewayFilterFactory")
  public GatewayFilterFactory<ApiAuthorizationIpGatewayFilterFactory.Config> apiAuthorizationIpGatewayFilterFactory() {
    return new ApiAuthorizationIpGatewayFilterFactory();
  }

  @Bean
  @ConditionalOnMissingBean(name = "apiAuthorizationQuotaGatewayFilterFactory")
  public GatewayFilterFactory<ApiAuthorizationQuotaGatewayFilterFactory.Config>
  apiAuthorizationQuotaGatewayFilterFactory(ApiRequestLogRepository apiRequestLogRepository) {

    return new ApiAuthorizationQuotaGatewayFilterFactory(apiRequestLogRepository);
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
  @Order(SecurityFilterChainProcessorOrders.OAUTH2)
  @ConditionalOnMissingBean(name = "oauth2ReactiveSecurityFilterChainProcessor")
  public GatewayServerSecurityFilterChainProcessor oauth2ReactiveSecurityFilterChainProcessor(
    ApiDocumentRepository apiDocumentRepository) {

    return new OAuth2ReactiveSecurityFilterChainProcessor(apiDocumentRepository);
  }

  @Bean
  @Order(SecurityFilterChainProcessorOrders.SWAGGER_PERMIT_ALL)
  @ConditionalOnMissingBean(name = "swaggerPermitAllSecurityFilterChainProcessor")
  public GatewayServerSecurityFilterChainProcessor swaggerPermitAllSecurityFilterChainProcessor(
    SwaggerUiConfigProperties properties) {

    String[] uris = new String[]{"/v3/api-docs/**", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html"};
    if (StringUtils.hasText(properties.getConfigUrl())) {
      uris = ArrayUtils.add(uris, properties.getConfigUrl());
    }
    return new PermitAllSecurityFilterChainProcessor(uris);
  }

  @Bean
  @ConditionalOnMissingBean(name = "gatewaySecurityWebFilterChain")
  public SecurityWebFilterChain gatewaySecurityWebFilterChain(
    ServerHttpSecurity http, List<GatewayServerSecurityFilterChainProcessor> processors) throws Exception {

    for (GatewayServerSecurityFilterChainProcessor processor : processors) {
      processor.preBuild(http);
    }

    SecurityWebFilterChain result = http.build();

    for (GatewayServerSecurityFilterChainProcessor processor : processors) {
      processor.postBuild(http);
    }

    return result;
  }

  @Bean
  public R2dbcCustomConversions r2dbcCustomConversions(ObjectMapper objectMapper) {
    return R2dbcCustomConversions.of(
      MySqlDialect.INSTANCE,
      new StringClientSettingsConverter(objectMapper)
    );
  }

  @Bean
  @ConditionalOnMissingBean(name = "swaggerConfigApi")
  public SwaggerConfigApi swaggerConfigApi(ObjectProvider<SwaggerConfigResource> swaggerConfigResources,
                                           ApiDocumentRepository apiDocumentRepository) {

    return new SwaggerConfigApi(swaggerConfigResources, apiDocumentRepository);
  }
}
