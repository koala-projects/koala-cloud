package cn.koala.cloud.gateway.filter;

import cn.koala.cloud.gateway.model.Api;
import cn.koala.cloud.gateway.model.ApiAuthorization;
import cn.koala.cloud.gateway.model.ApiRequestLog;
import cn.koala.cloud.gateway.model.RegisteredClient;
import cn.koala.cloud.gateway.repository.ApiRequestLogRepository;
import cn.koala.cloud.gateway.repository.ResourceRepository;
import cn.koala.cloud.gateway.web.ServerWebExchangeAttributeNames;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
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
    log.setClient(toJson(exchange.getAttribute(RegisteredClient.class.getName())));
    log.setRoute(toJson(exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR)));
    log.setApi(toJson(exchange.getAttribute(Api.class.getName())));
    log.setApiAuthorization(toJson(exchange.getAttribute(ApiAuthorization.class.getName())));
    log.setRequestUri(exchange.getRequest().getURI().toString());
    log.setRequestMethod(exchange.getRequest().getMethod().toString());
    log.setRequestQueryParams(toJson(exchange.getRequest().getQueryParams()));
    log.setRequestHeaders(toJson(exchange.getRequest().getHeaders()));
    log.setRequestToken(exchange.getRequest().getHeaders().getFirst("Authorization"));

    String requestBody = exchange.getAttribute(ServerWebExchangeAttributeNames.CACHED_REQUEST_BODY_STRING);
    if (StringUtils.hasText(requestBody)) {
      log.setRequestBody(requestBody);
    }

    Mono<ApiRequestLog> result = Mono.defer(() -> Mono.just(log));

    result = setResource(exchange, result);

    log.setLogTime(LocalDateTime.now());

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
    return FilterOrders.API_REQUEST_LOG;
  }
}
