package cn.koala.cloud.gateway;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 * 接口数据仓库接口
 *
 * @author Houtaroy
 */
public interface ApiRepository extends ReactiveCrudRepository<Api, Long> {

  Flux<Api> findByResourceIdAndIsEnabledAndIsDeleted(String resourceId, Integer isEnabled, Integer isDeleted);
}
