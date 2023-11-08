package cn.koala.cloud.gateway.filter.factory;

import cn.koala.cloud.gateway.filter.FilterOrders;
import cn.koala.cloud.gateway.model.ApiAuthorization;
import cn.koala.cloud.gateway.util.GatewayUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Optional;

/**
 * 接口授权IP校验网关过滤器工厂
 *
 * @author Houtaroy
 */
public class ApiAuthorizationIpGatewayFilterFactory extends AbstractGatewayFilterFactory<ApiAuthorizationIpGatewayFilterFactory.Config> {

  public ApiAuthorizationIpGatewayFilterFactory() {
    super(Config.class);
  }

  @Override
  public GatewayFilter apply(Config config) {
    return new OrderedGatewayFilter((exchange, chain) -> {
      ApiAuthorization authorization = exchange.getAttribute(ApiAuthorization.class.getName());
      if (authorization == null || CollectionUtils.isEmpty(authorization.getIps())) {
        return chain.filter(exchange);
      }
      if (authorization.getIps().contains(obtainIp(exchange.getRequest()))) {
        return chain.filter(exchange);
      }
      return GatewayUtils.setResponse(exchange, HttpStatus.FORBIDDEN, "IP地址不在白名单中");
    }, FilterOrders.API_AUTHORIZATION_IP);
  }

  private String obtainIp(ServerHttpRequest request) {
    String ip = request.getHeaders().getFirst("X-Forwarded-For");
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = Optional.ofNullable(request.getRemoteAddress())
        .map(InetSocketAddress::getAddress)
        .map(InetAddress::getHostAddress)
        .orElse(null);
    }
    return ip;
  }

  public static class Config {

  }
}
