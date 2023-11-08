package cn.koala.cloud.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * 接口请求日志
 *
 * @author Houtaroy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("k_api_request_log")
public class ApiRequestLog implements Persistable<String> {

  @Id
  private String id;

  private String clientId;

  private String client;

  private String routeId;

  private String route;

  private Long resourceId;

  private String resource;

  private Long apiId;

  private String api;

  private Long apiAuthorizationId;

  private String apiAuthorization;

  private String requestUri;

  private String requestMethod;

  private String requestQueryParams;

  private String requestHeaders;

  private String requestToken;

  private String requestBody;

  private LocalDateTime requestTime;

  private LocalDateTime logTime;

  @Override
  public boolean isNew() {
    return true;
  }
}
