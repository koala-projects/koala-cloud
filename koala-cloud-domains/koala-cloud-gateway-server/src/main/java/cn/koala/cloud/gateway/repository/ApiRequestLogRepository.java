package cn.koala.cloud.gateway.repository;

import cn.koala.cloud.gateway.model.ApiRequestLog;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * 接口请求日志仓库
 *
 * @author Houtaroy
 */
public interface ApiRequestLogRepository extends ReactiveCrudRepository<ApiRequestLog, String> {

}
