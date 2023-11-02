package cn.koala.cloud.gateway;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * 接口响应日志仓库
 *
 * @author Houtaroy
 */
public interface ApiResponseLogRepository extends ReactiveCrudRepository<ApiResponseLog, String> {

}
