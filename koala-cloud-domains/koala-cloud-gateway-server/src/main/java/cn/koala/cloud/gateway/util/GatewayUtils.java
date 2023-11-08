package cn.koala.cloud.gateway.util;

import cn.koala.cloud.gateway.model.Api;
import cn.koala.cloud.gateway.model.RegisteredClient;
import cn.koala.persist.domain.YesNo;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 网关工具类
 *
 * @author Houtaroy
 */
public abstract class GatewayUtils {

  private static final String RESPONSE_MESSAGE_TEMPLATE = """
    {
      "code": %d,
      "message": "%s"
    }
    """;

  public static boolean isPlain(Api api, RegisteredClient client) {
    return (api == null || api.getIsSupportEncrypt() != YesNo.YES.getValue())
      || (client == null || !client.isEncrypted());
  }

  public static Mono<Void> setResponse(ServerWebExchange exchange, HttpStatus status, String message) {
    ServerWebExchangeUtils.setResponseStatus(exchange, HttpStatus.TOO_MANY_REQUESTS);
    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
    String body = String.format(RESPONSE_MESSAGE_TEMPLATE, status.value(), message);
    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
    return exchange.getResponse().writeWith(Mono.just(buffer));
  }
}
