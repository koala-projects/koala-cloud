package cn.koala.cloud.gateway;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * 注册客户端
 *
 * @author Houtaroy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("oauth2_registered_client")
public class RegisteredClient {

  @Id
  private String id;

  private String clientId;

  private LocalDateTime clientIdIssuedAt;

  private String clientSecret;

  private LocalDateTime clientSecretExpiresAt;

  private String clientName;

  private String clientAuthenticationMethods;

  private String authorizationGrantTypes;

  private String redirectUris;

  private String scopes;

  private String clientSettings;

  private String tokenSettings;
}
