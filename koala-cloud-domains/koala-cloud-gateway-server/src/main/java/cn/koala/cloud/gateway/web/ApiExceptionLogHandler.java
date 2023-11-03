package cn.koala.cloud.gateway.web;

import cn.koala.cloud.gateway.model.ApiExceptionLog;
import cn.koala.cloud.gateway.repository.ApiExceptionLogRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * 接口异常日志处理器
 *
 * @author Houtaroy
 */
@RequiredArgsConstructor
public class ApiExceptionLogHandler implements WebExceptionHandler {

  private final ApiExceptionLogRepository repository;

  @Override
  @NonNull
  public Mono<Void> handle(@NonNull ServerWebExchange exchange, @NonNull Throwable ex) {
    return repository.save(obtainLog(exchange, ex)).flatMap(data -> Mono.error(ex));
  }

  private ApiExceptionLog obtainLog(ServerWebExchange exchange, Throwable ex) {
    ApiExceptionLog result = new ApiExceptionLog();
    result.setId(exchange.getAttribute(ServerWebExchange.LOG_ID_ATTRIBUTE));
    result.setMessage(ex.getLocalizedMessage());
    result.setLogTime(LocalDateTime.now());
    return result;
  }
}
