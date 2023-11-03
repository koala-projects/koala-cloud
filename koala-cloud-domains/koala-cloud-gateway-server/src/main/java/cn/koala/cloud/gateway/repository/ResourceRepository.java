package cn.koala.cloud.gateway.repository;

import cn.koala.cloud.gateway.model.Resource;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * 资源仓库
 *
 * @author Houtaroy
 */
public interface ResourceRepository extends ReactiveCrudRepository<Resource, Long> {

}
