package cn.koala.cloud.gateway.filter;

import cn.koala.cloud.gateway.Api;
import cn.koala.cloud.gateway.ApiAuthorization;
import cn.koala.cloud.gateway.ApiRequestLog;
import cn.koala.cloud.gateway.ApiRequestLogRepository;
import cn.koala.cloud.gateway.RegisteredClientRepository;
import cn.koala.cloud.gateway.ResourceRepository;
import cn.koala.cloud.gateway.RouteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * 接口请求日志全局过滤器
 *
 * @author Houtaroy
 */
@RequiredArgsConstructor
public class ApiRequestLogGlobalFilter implements GlobalFilter, Ordered {

  private final ApiRequestLogRepository apiRequestLogRepository;
  private final RegisteredClientRepository clientRepository;
  private final RouteRepository routeRepository;
  private final ResourceRepository resourceRepository;
  private final ObjectMapper objectMapper;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    return obtainLog(exchange).flatMap(log -> apiRequestLogRepository.save(log).then(chain.filter(exchange)));
  }

  public Mono<ApiRequestLog> obtainLog(ServerWebExchange exchange) {

    ApiRequestLog log = new ApiRequestLog();

    log.setId(exchange.getAttribute(ServerWebExchange.LOG_ID_ATTRIBUTE));
    log.setRequestTime(LocalDateTime.now());
    log.setApi(toJson(exchange.getAttribute(Api.class.getName())));
    log.setApiAuthorization(toJson(exchange.getAttribute(ApiAuthorization.class.getName())));
    log.setRequestUri(exchange.getRequest().getURI().toString());
    log.setRequestMethod(exchange.getRequest().getMethod().toString());
    log.setRequestQueryParams(toJson(exchange.getRequest().getQueryParams()));
    log.setRequestHeaders(toJson(exchange.getRequest().getHeaders()));
    log.setRequestToken(exchange.getRequest().getHeaders().getFirst("Authorization"));

    String requestBody = exchange.getAttribute(ApiLogPreparationGlobalFilter.CACHED_REQUEST_BODY_STRING_ATTR);
    if (StringUtils.hasText(requestBody)) {
      log.setRequestBody(requestBody);
    }

    Mono<ApiRequestLog> result = Mono.defer(() -> Mono.just(log));

    result = setClient(exchange, result);

    result = setRoute(exchange, result);

    result = setResource(exchange, result);

    log.setLogTime(LocalDateTime.now());

    return result;
  }

  private Mono<ApiRequestLog> setClient(ServerWebExchange exchange, Mono<ApiRequestLog> result) {
    String clientId = exchange.getAttribute(OAuth2ParameterNames.CLIENT_ID);
    if (StringUtils.hasText(clientId)) {
      result = result.flatMap(log ->
        clientRepository.findByClientId(clientId).map(client -> {
          log.setClient(toJson(client));
          return log;
        })
      );
    }
    return result;
  }

  private Mono<ApiRequestLog> setRoute(ServerWebExchange exchange, Mono<ApiRequestLog> result) {
    Route gatewayRoute = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
    if (gatewayRoute != null && StringUtils.hasText(gatewayRoute.getId())) {
      result = result.flatMap(log ->
        routeRepository.findById(gatewayRoute.getId()).map(route -> {
          log.setRoute(toJson(route));
          return log;
        })
      );
    }
    return result;
  }

  private Mono<ApiRequestLog> setResource(ServerWebExchange exchange, Mono<ApiRequestLog> result) {
    Api api = exchange.getAttribute(Api.class.getName());
    if (api != null) {
      result = result.flatMap(log ->
        resourceRepository.findById(api.getResourceId()).map(resource -> {
          log.setResource(toJson(resource));
          return log;
        })
      );
    }
    return result;
  }


  private String toJson(Object object) {
    if (object == null) {
      return null;
    }
    try {
      return objectMapper.writeValueAsString(object);
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public int getOrder() {
    return 1200;
  }
}
