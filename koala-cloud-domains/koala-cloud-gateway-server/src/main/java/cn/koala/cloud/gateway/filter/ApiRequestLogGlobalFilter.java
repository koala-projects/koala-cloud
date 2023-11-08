package cn.koala.cloud.gateway.filter;

import cn.koala.cloud.gateway.model.Api;
import cn.koala.cloud.gateway.model.ApiAuthorization;
import cn.koala.cloud.gateway.model.ApiRequestLog;
import cn.koala.cloud.gateway.model.RegisteredClient;
import cn.koala.cloud.gateway.model.Resource;
import cn.koala.cloud.gateway.repository.ApiRequestLogRepository;
import cn.koala.cloud.gateway.web.ServerWebExchangeAttributeNames;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 接口请求日志全局过滤器
 *
 * @author Houtaroy
 */
@RequiredArgsConstructor
public class ApiRequestLogGlobalFilter implements GlobalFilter, Ordered {

  private final ApiRequestLogRepository apiRequestLogRepository;
  private final ObjectMapper objectMapper;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    return apiRequestLogRepository.save(obtainLog(exchange)).then(chain.filter(exchange));
  }

  public ApiRequestLog obtainLog(ServerWebExchange exchange) {

    ApiRequestLog result = new ApiRequestLog();

    result.setId(exchange.getAttribute(ServerWebExchange.LOG_ID_ATTRIBUTE));
    result.setRequestTime(LocalDateTime.now());

    RegisteredClient client = exchange.getAttribute(RegisteredClient.class.getName());
    result.setClientId(Optional.ofNullable(client).map(RegisteredClient::getClientId).orElse(null));
    result.setClient(toJson(client));

    Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
    result.setRouteId(Optional.ofNullable(route).map(Route::getId).orElse(null));
    result.setRoute(toJson(route));

    Api api = exchange.getAttribute(Api.class.getName());
    result.setApiId(Optional.ofNullable(api).map(Api::getId).orElse(null));
    result.setApi(toJson(api));

    ApiAuthorization apiAuthorization = exchange.getAttribute(ApiAuthorization.class.getName());
    result.setApiAuthorizationId(Optional.ofNullable(apiAuthorization).map(ApiAuthorization::getId).orElse(null));
    result.setApiAuthorization(toJson(apiAuthorization));

    Resource resource = exchange.getAttribute(Resource.class.getName());
    result.setResourceId(Optional.ofNullable(resource).map(Resource::getId).orElse(null));
    result.setResource(toJson(resource));

    result.setRequestUri(exchange.getRequest().getURI().toString());
    result.setRequestMethod(exchange.getRequest().getMethod().toString());
    result.setRequestQueryParams(toJson(exchange.getRequest().getQueryParams()));
    result.setRequestHeaders(toJson(exchange.getRequest().getHeaders()));
    result.setRequestToken(exchange.getRequest().getHeaders().getFirst("Authorization"));

    String requestBody = exchange.getAttribute(ServerWebExchangeAttributeNames.CACHED_REQUEST_BODY_STRING);
    if (StringUtils.hasText(requestBody)) {
      result.setRequestBody(requestBody);
    }

    result.setLogTime(LocalDateTime.now());

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
