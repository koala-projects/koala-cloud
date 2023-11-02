package cn.koala.cloud.gateway;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * 资源仓库
 *
 * @author Houtaroy
 */
public interface ResourceRepository extends ReactiveCrudRepository<Resource, Long> {

}
