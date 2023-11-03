package cn.koala.cloud.gateway.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 路由工具类
 *
 * @author Houtaroy
 */
public abstract class RouteUtils {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  private static final TypeReference<List<PredicateDefinition>> PREDICATE_DEFINITION_LIST_TYPE = new TypeReference<>() {
  };

  private static final TypeReference<List<FilterDefinition>> FILTER_DEFINITION_LIST_TYPE = new TypeReference<>() {
  };

  private static final TypeReference<Map<String, Object>> METADATA_TYPE = new TypeReference<>() {
  };

  public static List<PredicateDefinition> json2PredicateDefinitions(String json) {
    if (!StringUtils.hasText(json)) {
      return new ArrayList<>();
    }
    try {
      return new ArrayList<>(MAPPER.readValue(json, PREDICATE_DEFINITION_LIST_TYPE));
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("无效的断言格式", e);
    }
  }

  public static List<FilterDefinition> json2FilterDefinitions(String json) {
    if (!StringUtils.hasText(json)) {
      return new ArrayList<>();
    }
    try {
      return new ArrayList<>(MAPPER.readValue(json, FILTER_DEFINITION_LIST_TYPE));
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("无效的过滤器格式", e);
    }
  }

  public static Map<String, Object> json2Metadata(String json) {
    if (!StringUtils.hasText(json)) {
      return new HashMap<>();
    }
    try {
      return MAPPER.readValue(json, METADATA_TYPE);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("无效的元数据格式", e);
    }
  }

  public static Long obtainResourceId(Route route) {
    return Optional.ofNullable(route.getMetadata().get(cn.koala.cloud.gateway.model.Route.METADATA_RESOURCE_ID_KEY))
      .map(Object::toString)
      .map(Long::valueOf)
      .orElse(null);
  }
}
