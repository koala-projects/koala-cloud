package cn.koala.cloud.service.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 示例服务接口文档配置
 *
 * @author Houtaroy
 */
@Configuration
public class OpenApiConfig {

  @Bean
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
