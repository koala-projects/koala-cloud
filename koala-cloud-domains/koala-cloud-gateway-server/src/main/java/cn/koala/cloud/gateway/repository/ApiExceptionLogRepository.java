package cn.koala.cloud.gateway.repository;

import cn.koala.cloud.gateway.model.ApiExceptionLog;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * 接口异常日志仓库
 *
 * @author Houtaroy
 */
public interface ApiExceptionLogRepository extends ReactiveCrudRepository<ApiExceptionLog, String> {

}
