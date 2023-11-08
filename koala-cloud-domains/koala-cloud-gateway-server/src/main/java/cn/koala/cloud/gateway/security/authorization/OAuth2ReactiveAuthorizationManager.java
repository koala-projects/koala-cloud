package cn.koala.cloud.gateway.security.authorization;

import cn.koala.cloud.gateway.model.ApiDocument;
import cn.koala.cloud.gateway.repository.ApiDocumentRepository;
import cn.koala.persist.domain.YesNo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * OAuth2授权管理器
 *
 * @author Houtaroy
 */
@RequiredArgsConstructor
public class OAuth2ReactiveAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

  private final AuthenticationTrustResolver authTrustResolver = new AuthenticationTrustResolverImpl();
  private final ApiDocumentRepository apiDocumentRepository;

  @Override
  public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext object) {
    return authentication.filter(this::isNotAnonymous)
      .doOnNext(auth -> addExchangeAttributes(auth, object))
      .map(this::getAuthorizationDecision)
      .switchIfEmpty(Mono.defer(() -> obtainEmptyAuthorizationDecision(object)));
  }

  /**
   * Verify (via {@link AuthenticationTrustResolver}) that the given authentication is
   * not anonymous.
   *
   * @param authentication to be checked
   * @return <code>true</code> if not anonymous, otherwise <code>false</code>.
   */
  private boolean isNotAnonymous(Authentication authentication) {
    return !this.authTrustResolver.isAnonymous(authentication);
  }

  private void addExchangeAttributes(Authentication authentication, AuthorizationContext object) {
    if (authentication.getPrincipal() instanceof Jwt jwt) {
      Map<String, Object> attributes = object.getExchange().getAttributes();

      AuthorizationGrantType grantType = new AuthorizationGrantType(jwt.getClaimAsString("grant_type"));
      attributes.put(AuthorizationGrantType.class.getName(), grantType);

      if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(grantType)) {
        attributes.put(OAuth2ParameterNames.CLIENT_ID, jwt.getClaimAsString("sub"));
      }
    }
  }

  private AuthorizationDecision getAuthorizationDecision(Authentication authentication) {
    return new AuthorizationDecision(authentication.isAuthenticated());
  }

  private Mono<AuthorizationDecision> obtainEmptyAuthorizationDecision(AuthorizationContext object) {
    return isApiDocument(object.getExchange()).map(AuthorizationDecision::new);
  }

  private Mono<Boolean> isApiDocument(ServerWebExchange exchange) {
    return apiDocumentRepository.findByIsEnabledAndIsDeleted(YesNo.YES.getValue(), YesNo.NO.getValue())
      .map(ApiDocument::getUri)
      .collectList()
      .map(uris -> uris.contains(exchange.getRequest().getPath().value()));
  }
}
