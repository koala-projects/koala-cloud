package cn.koala.cloud.gateway.web;

import lombok.NonNull;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

/**
 * 自定义请求体请求包装器
 *
 * @author Houtaroy
 */
public class CustomRequestBodyDecorator extends ServerHttpRequestDecorator {

  private final byte[] body;

  public CustomRequestBodyDecorator(ServerHttpRequest delegate, byte[] body) {
    super(delegate);
    this.body = body;
  }

  @Override
  @NonNull
  public HttpHeaders getHeaders() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.putAll(getDelegate().getHeaders());
    if (body.length > 0) {
      httpHeaders.setContentLength(body.length);
    }
    return httpHeaders;
  }

  @Override
  @NonNull
  public Flux<DataBuffer> getBody() {
    return Flux.just(new DefaultDataBufferFactory().wrap(body));
  }
}
