package cn.koala.cloud.gateway.filter;

import cn.koala.cloud.gateway.web.ServerWebExchangeAttributeNames;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 缓存请求体字符串全局过滤器
 *
 * @author Houtaroy
 */
@Component
public class CacheRequestBodyStringGlobalFilter implements GlobalFilter, Ordered {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    return ServerWebExchangeUtils.cacheRequestBody(exchange, (serverHttpRequest) ->
      serverHttpRequest.getBody().collectList().map(dataBuffers -> {
        StringBuilder sb = new StringBuilder();
        dataBuffers.forEach(dataBuffer -> {
          byte[] bytes = new byte[dataBuffer.readableByteCount()];
          dataBuffer.read(bytes);
          sb.append(new String(bytes, StandardCharsets.UTF_8));
        });
        exchange.getAttributes().put(ServerWebExchangeAttributeNames.CACHED_REQUEST_BODY_STRING, sb.toString());
        return serverHttpRequest;
      })
    ).flatMap(request -> chain.filter(exchange.mutate().request(request).build()));
  }

  @Override
  public int getOrder() {
    return FilterOrders.CACHE_REQUEST_BODY_STRING;
  }
}
