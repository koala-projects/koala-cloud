package cn.koala.cloud.gateway;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * 接口异常日志仓库
 *
 * @author Houtaroy
 */
public interface ApiExceptionLogRepository extends ReactiveCrudRepository<ApiExceptionLog, String> {

}
