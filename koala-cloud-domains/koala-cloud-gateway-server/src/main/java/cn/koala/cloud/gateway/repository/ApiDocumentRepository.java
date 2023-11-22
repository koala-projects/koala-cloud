package cn.koala.cloud.gateway.repository;

import cn.koala.cloud.gateway.model.ApiDocument;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 接口文档仓库接口
 *
 * @author Houtaroy
 */
public interface ApiDocumentRepository extends ReactiveCrudRepository<ApiDocument, String> {

  Flux<ApiDocument> findByIsEnabledAndIsDeleted(Integer isEnabled, Integer isDeleted);
  
  Mono<ApiDocument> findByUriAndIsEnabledAndIsDeleted(String uri, Integer isEnabled, Integer isDeleted);
}
