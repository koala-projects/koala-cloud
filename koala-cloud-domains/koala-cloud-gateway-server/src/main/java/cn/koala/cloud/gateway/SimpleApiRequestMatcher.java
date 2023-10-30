package cn.koala.cloud.gateway;

import lombok.NonNull;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 接口请求匹配器简单实现
 * <p>
 * 内部整合了{@link PathPatternParser}和一个Pattern缓存
 * <p>
 * 当请求方法和请求路径匹配时, 判断接口与请求匹配
 *
 * @author Houtaroy
 */
public class SimpleApiRequestMatcher implements ApiRequestMatcher {

  private final PathPatternParser parser = new PathPatternParser();
  private final Map<Long, PathPattern> patterns = new ConcurrentHashMap<>();

  @Override
  public boolean matches(@NonNull Api api, @NonNull ServerHttpRequest request) {
    return request.getMethod().matches(api.getMethod())
      && patterns.computeIfAbsent(api.getId(), key -> parser.parse(api.getPath())).matches(request.getPath());
  }
}
