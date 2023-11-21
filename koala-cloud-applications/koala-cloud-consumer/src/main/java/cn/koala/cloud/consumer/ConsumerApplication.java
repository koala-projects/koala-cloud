package cn.koala.cloud.consumer;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 消费者应用
 *
 * @author Houtaroy
 */
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@RestController
@RequiredArgsConstructor
public class ConsumerApplication {

  @FeignClient(name = "koala-cloud-provider")
  public interface TestService {

    @GetMapping(value = "/test")
    String test();
  }

  private final TestService testService;

  @RequestMapping("/test/{id}")
  public String test(@PathVariable("id") Long id) {
    return this.testService.test();
  }

  @PostMapping("/test/post")
  public ResponseEntity<Map<String, Object>> post(@RequestBody Map<String, Object> body) {
    return ResponseEntity.ok(Map.of("data", "test"));
  }

  public static void main(String[] args) {
    SpringApplication.run(ConsumerApplication.class, args);
  }

  @Bean
  @ConditionalOnMissingBean(name = "gatewayOpenApi")
  public OpenAPI gatewayOpenApi() {
    return new OpenAPI()
      .info(new Info().title("消费者服务").version("1.0.0"))
      .components(
        new Components().addSecuritySchemes(
          "spring-security",
          new SecurityScheme()
            .type(SecurityScheme.Type.OAUTH2)
            .scheme("bearer")
            .bearerFormat("JWT")
            .name("Bearer")
            .flows(
              new OAuthFlows()
                .clientCredentials(
                  new OAuthFlow()
                    .authorizationUrl("/oauth2/authorize")
                    .tokenUrl("/oauth2/token")
                )
            )
        )
      );
  }
}
