package cn.koala.cloud.gateway.web;

import cn.koala.cloud.gateway.model.Api;
import lombok.NonNull;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.util.pattern.PathPatternParser;

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

  @Override
  public boolean matches(@NonNull Api api, @NonNull ServerHttpRequest request) {
    return request.getMethod().matches(api.getMethod()) && parser.parse(api.getPath()).matches(request.getPath());
  }
}
