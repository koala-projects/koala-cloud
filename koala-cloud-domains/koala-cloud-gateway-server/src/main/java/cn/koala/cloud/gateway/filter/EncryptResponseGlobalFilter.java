package cn.koala.cloud.gateway.filter;

import cn.hutool.crypto.symmetric.SM4;
import cn.koala.cloud.gateway.model.Api;
import cn.koala.cloud.gateway.model.RegisteredClient;
import cn.koala.cloud.gateway.util.GatewayUtils;
import cn.koala.cloud.gateway.web.EncryptBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 加密响应全局过滤器
 *
 * @author Houtaroy
 */
@RequiredArgsConstructor
public class EncryptResponseGlobalFilter implements GlobalFilter, Ordered {

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
        Api api = exchange.getAttribute(Api.class.getName());
        RegisteredClient client = exchange.getAttribute(RegisteredClient.class.getName());
        if (client == null || GatewayUtils.isPlain(api, client)) {
          return super.writeWith(body);
        }
        return super.writeWith(DataBufferUtils.join(body).map(dataBuffer -> {
          byte[] content = new byte[dataBuffer.readableByteCount()];
          dataBuffer.read(content);
          DataBufferUtils.release(dataBuffer);
          String encryptedBody = encrypt(content, client.getSm4Key());
          getDelegate().getHeaders().setContentType(MediaType.APPLICATION_JSON);
          getDelegate().getHeaders().set(HttpHeaders.CONTENT_LENGTH, String.valueOf(encryptedBody.length()));
          return bufferFactory().wrap(encryptedBody.getBytes(StandardCharsets.UTF_8));
        }));
      }
    };
  }

  private String encrypt(byte[] body, String key) {
    String encrypted = new SM4(Base64.getDecoder().decode(key)).encryptBase64(body);
    try {
      return objectMapper.writeValueAsString(new EncryptBody(encrypted));
    } catch (Exception e) {
      throw new IllegalArgumentException("数据加密失败, 请联系管理员", e);
    }
  }

  @Override
  public int getOrder() {
    return FilterOrders.ENCRYPT_RESPONSE;
  }
}
