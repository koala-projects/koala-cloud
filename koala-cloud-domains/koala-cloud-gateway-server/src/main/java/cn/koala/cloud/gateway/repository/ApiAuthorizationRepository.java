package cn.koala.cloud.gateway.repository;

import cn.koala.cloud.gateway.model.ApiAuthorization;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * 接口数据仓库接口
 *
 * @author Houtaroy
 */
public interface ApiAuthorizationRepository extends ReactiveCrudRepository<ApiAuthorization, Long> {

  Mono<ApiAuthorization> findByClientIdAndApiIdAndExpiresAtAfterAndIsDeleted(String resourceId, Long apiId,
                                                                             LocalDate expiresAt, Integer isDeleted);
}
