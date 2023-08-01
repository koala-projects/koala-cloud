package cn.koala.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * TODO: 修改类描述
 *
 * @author Houtaroy
 */
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

  // @RequestMapping("/circuitbreakerfallback")
  // public String circuitbreakerfallback() {
  //   return "This is a fallback";
  // }
  //
  // @Bean
  // public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
  //   //@formatter:off
  //   return builder.routes()
  //     .route("path_route", r -> r.path("/get")
  //       .uri("http://httpbin.org"))
  //     .route("host_route", r -> r.host("*.myhost.org")
  //       .uri("http://httpbin.org"))
  //     .route("rewrite_route", r -> r.host("*.rewrite.org")
  //       .filters(f -> f.rewritePath("/foo/(?<segment>.*)",
  //         "/${segment}"))
  //       .uri("http://httpbin.org"))
  //     .route("circuitbreaker_route", r -> r.host("*.circuitbreaker.org")
  //       .filters(f -> f.circuitBreaker(c -> c.setName("slowcmd")))
  //       .uri("http://httpbin.org"))
  //     .route("circuitbreaker_fallback_route", r -> r.host("*.circuitbreakerfallback.org")
  //       .filters(f -> f.circuitBreaker(c -> c.setName("slowcmd").setFallbackUri("forward:/circuitbreakerfallback")))
  //       .uri("http://httpbin.org"))
  //     .route("websocket_route", r -> r.path("/echo")
  //       .uri("ws://localhost:9000"))
  //     .build();
  //   //@formatter:on
  // }

  public static void main(String[] args) {
    SpringApplication.run(GatewayApplication.class, args);
  }
}
