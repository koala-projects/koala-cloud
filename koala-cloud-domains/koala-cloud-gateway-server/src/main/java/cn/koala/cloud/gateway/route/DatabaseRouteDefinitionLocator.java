package cn.koala.cloud.gateway.route;

import cn.koala.cloud.gateway.model.Route;
import cn.koala.cloud.gateway.repository.RouteRepository;
import cn.koala.persist.domain.YesNo;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import reactor.core.publisher.Flux;

/**
 * 数据库路由定义定位器
 *
 * @author Houtaroy
 */
@RequiredArgsConstructor
public class DatabaseRouteDefinitionLocator implements RouteDefinitionLocator {

  private final RouteRepository routeRepository;

  @Override
  public Flux<RouteDefinition> getRouteDefinitions() {
    return routeRepository.findByIsDeleted(YesNo.NO.getValue()).map(Route::toRouteDefinition);
  }
}
