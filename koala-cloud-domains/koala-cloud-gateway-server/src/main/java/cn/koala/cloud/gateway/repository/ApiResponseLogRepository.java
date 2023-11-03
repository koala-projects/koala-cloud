package cn.koala.cloud.gateway.repository;

import cn.koala.cloud.gateway.model.ApiResponseLog;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * 接口响应日志仓库
 *
 * @author Houtaroy
 */
public interface ApiResponseLogRepository extends ReactiveCrudRepository<ApiResponseLog, String> {

}
