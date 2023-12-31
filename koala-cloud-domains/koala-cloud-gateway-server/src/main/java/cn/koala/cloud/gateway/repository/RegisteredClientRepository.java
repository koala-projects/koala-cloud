package cn.koala.cloud.gateway.repository;

import cn.koala.cloud.gateway.model.RegisteredClient;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * 注册客户端仓库
 *
 * @author Houtaroy
 */
public interface RegisteredClientRepository extends ReactiveCrudRepository<RegisteredClient, String> {

  Mono<RegisteredClient> findByClientId(String clientId);
}
