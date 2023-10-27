package cn.koala.cloud.gateway;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 * 接口数据仓库接口
 *
 * @author Houtaroy
 */
public interface RouteRepository extends ReactiveCrudRepository<Route, String> {

  Flux<Route> findByIsDeleted(Integer isDeleted);
}
