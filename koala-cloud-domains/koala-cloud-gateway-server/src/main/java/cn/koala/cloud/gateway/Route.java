package cn.koala.cloud.gateway;

import cn.koala.cloud.gateway.support.RouteUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.net.URI;
import java.util.Map;

/**
 * 路由实体类
 *
 * @author Houtaroy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("k_route")
public class Route {

  public static final String METADATA_RESOURCE_ID_KEY = "resource-id";

  @Id
  private String id;

  private String name;

  private String resourceId;

  private String uri;

  private String predicates;

  private String filters;

  private String metadata;

  private Integer sortIndex;

  private Integer isDeleted;

  public RouteDefinition toRouteDefinition() {
    RouteDefinition result = new RouteDefinition();
    result.setId(id);
    result.setUri(URI.create(uri));
    result.setOrder(sortIndex);
    result.setPredicates(RouteUtils.yaml2PredicateDefinitions(predicates));
    result.setFilters(RouteUtils.yaml2FilterDefinitions(filters));
    Map<String, Object> metadata = RouteUtils.yaml2Metadata(this.metadata);
    metadata.put(METADATA_RESOURCE_ID_KEY, resourceId);
    result.setMetadata(metadata);
    return result;
  }
}
