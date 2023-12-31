package cn.koala.cloud.gateway.filter;

import cn.hutool.crypto.symmetric.SM4;
import cn.koala.cloud.gateway.model.Api;
import cn.koala.cloud.gateway.model.RegisteredClient;
import cn.koala.cloud.gateway.util.GatewayUtils;
import cn.koala.cloud.gateway.web.CustomRequestBodyDecorator;
import cn.koala.cloud.gateway.web.EncryptBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

/**
 * 解密请求全局过滤器
 *
 * @author Houtaroy
 */
@Component
@RequiredArgsConstructor
public class DecryptRequestGlobalFilter implements GlobalFilter, Ordered {

  private static final List<HttpMethod> supportMethods = List.of(HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH);

  private final ObjectMapper objectMapper;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

    if (!supportMethods.contains(exchange.getRequest().getMethod())) {
      return chain.filter(exchange);
    }

    Api api = exchange.getAttribute(Api.class.getName());
    RegisteredClient client = exchange.getAttribute(RegisteredClient.class.getName());
    if (client == null || GatewayUtils.isPlain(api, client)) {
      return chain.filter(exchange);
    }

    String sm4Key = client.getSm4Key();
    if (!StringUtils.hasText(sm4Key)) {
      return GatewayUtils.setResponse(exchange, HttpStatus.BAD_REQUEST, "获取客户端密钥失败");
    }

    return DataBufferUtils.join(exchange.getRequest().getBody())
      .flatMap(dataBuffer -> decrypt(dataBuffer, sm4Key))
      .map(decryptedBody -> new CustomRequestBodyDecorator(exchange.getRequest(), decryptedBody))
      .flatMap(request -> chain.filter(exchange.mutate().request(request).build()));
  }

  private Mono<byte[]> decrypt(DataBuffer dataBuffer, String key) {
    byte[] body = new byte[dataBuffer.readableByteCount()];
    dataBuffer.read(body);
    DataBufferUtils.release(dataBuffer);
    String encryptBody;
    try {
      encryptBody = objectMapper.readValue(body, EncryptBody.class).getData();
    } catch (IOException e) {
      return Mono.error(new IllegalArgumentException("获取加密数据失败, 请联系管理员"));
    }
    if (!StringUtils.hasText(encryptBody)) {
      return Mono.error(new IllegalArgumentException("获取加密数据失败, 请联系管理员"));
    }
    return Mono.just(new SM4(Base64.getDecoder().decode(key)).decrypt(encryptBody));
  }

  @Override
  public int getOrder() {
    return FilterOrders.DECRYPT_REQUEST;
  }
}
