package cn.koala.cloud.gateway.web;

import cn.koala.cloud.gateway.model.Api;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * 接口请求匹配器
 *
 * @author Houtaroy
 */
public interface ApiRequestMatcher {

  boolean matches(Api api, ServerHttpRequest request);
}
