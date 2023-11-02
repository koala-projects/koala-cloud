package cn.koala.cloud.gateway;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * 接口请求日志仓库
 *
 * @author Houtaroy
 */
public interface ApiRequestLogRepository extends ReactiveCrudRepository<ApiRequestLog, String> {

}
