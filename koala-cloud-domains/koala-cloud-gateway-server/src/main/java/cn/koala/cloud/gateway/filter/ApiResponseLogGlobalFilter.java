package cn.koala.cloud.gateway.filter;

import cn.koala.cloud.gateway.model.ApiResponseLog;
import cn.koala.cloud.gateway.repository.ApiResponseLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 接口响应日志全局过滤器
 *
 * @author Houtaroy
 */
@RequiredArgsConstructor
public class ApiResponseLogGlobalFilter implements GlobalFilter, Ordered {

  private final ApiResponseLogRepository apiResponseLogRepository;
  private final ObjectMapper objectMapper;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    return chain.filter(exchange.mutate().response(decorate(exchange)).build());
  }

  private ServerHttpResponse decorate(ServerWebExchange exchange) {
    return new ServerHttpResponseDecorator(exchange.getResponse()) {
      @Override
      @NonNull
      public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
        ApiResponseLog log = obtainLog(getDelegate());
        log.setId(exchange.getAttribute(ServerWebExchange.LOG_ID_ATTRIBUTE));
        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
        return super.writeWith(
            fluxBody.buffer().map(dataBuffers -> {
              DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
              DataBuffer join = dataBufferFactory.join(dataBuffers);
              byte[] content = new byte[join.readableByteCount()];
              join.read(content);
              // 释放掉内存
              DataBufferUtils.release(join);

              log.setResponseBody(new String(content, StandardCharsets.UTF_8));
              return bufferFactory().wrap(content);
            })
          )
          .then(Mono.fromRunnable(() -> log.setLogTime(LocalDateTime.now())))
          .then(apiResponseLogRepository.save(log))
          .then();
      }
    };
  }

  private ApiResponseLog obtainLog(ServerHttpResponse response) {
    ApiResponseLog result = new ApiResponseLog();
    result.setResponseTime(LocalDateTime.now());
    result.setResponseCode(Optional.ofNullable(response.getStatusCode()).map(HttpStatusCode::value).orElse(null));
    result.setResponseHeaders(toJson(response.getHeaders()));
    result.setResponseCookies(toJson(response.getCookies()));
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
    return FilterOrders.API_RESPONSE_LOG;
  }
}
