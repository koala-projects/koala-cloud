package cn.koala.cloud.gateway.repository;

import cn.koala.cloud.gateway.model.OAuth2Authorization;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * 接口数据仓库接口
 *
 * @author Houtaroy
 */
public interface OAuth2AuthorizationRepository extends ReactiveCrudRepository<OAuth2Authorization, String> {

  Mono<OAuth2Authorization> findByAccessTokenValue(String accessTokenValue);
}
