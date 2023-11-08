package cn.koala.cloud.gateway.springdoc;

import cn.koala.cloud.gateway.model.ApiDocument;
import cn.koala.cloud.gateway.repository.ApiDocumentRepository;
import cn.koala.persist.domain.YesNo;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.webflux.ui.SwaggerConfigResource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 网关Swagger配置接口
 *
 * @author Houtaroy
 */
@RestController
@RequestMapping("gateway")
@RequiredArgsConstructor
public class SwaggerConfigApi {

  private final ObjectProvider<SwaggerConfigResource> resourceObjectProvider;
  private final ApiDocumentRepository apiDocumentRepository;

  @GetMapping("swagger-config")
  public Mono<Map<String, Object>> swagger(ServerHttpRequest request) {
    SwaggerConfigResource swaggerConfigResource = resourceObjectProvider.getIfAvailable();
    if (swaggerConfigResource == null) {
      return Mono.error(new IllegalStateException("未找到Swagger配置资源"));
    }
    Map<String, Object> result = swaggerConfigResource.getSwaggerUiConfig(request);
    return apiDocumentRepository.findByIsEnabledAndIsDeleted(YesNo.YES.getValue(), YesNo.NO.getValue())
      .map(this::toSwaggerUrl)
      .collect(Collectors.toCollection(LinkedHashSet::new))
      .map(urls -> {
        result.put("urls", urls);
        return result;
      });
  }

  private AbstractSwaggerUiConfigProperties.SwaggerUrl toSwaggerUrl(ApiDocument apiDocument) {
    AbstractSwaggerUiConfigProperties.SwaggerUrl result = new AbstractSwaggerUiConfigProperties.SwaggerUrl();
    result.setUrl(apiDocument.getUri());
    result.setName(apiDocument.getCode());
    result.setDisplayName(apiDocument.getName());
    return result;
  }
}
