package cn.koala.cloud.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import java.time.LocalDateTime;
import java.util.Optional;

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

  private static final String ENCRYPTED = "encrypted";
  private static final String SM2_PUBLIC_KEY = "sm2-public-key";
  private static final String SM4_KEY = "sm4-key";

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

  private ClientSettings clientSettings;

  private String tokenSettings;

  public boolean isEncrypted() {
    return Optional.ofNullable(clientSettings.getSetting(ENCRYPTED))
      .map(Object::toString)
      .map(Boolean::parseBoolean)
      .orElse(false);
  }

  public String getSm2PublicKey() {
    return clientSettings.getSetting(SM2_PUBLIC_KEY);
  }

  public String getSm4Key() {
    return clientSettings.getSetting(SM4_KEY);
  }
}
