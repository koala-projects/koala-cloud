package cn.koala.cloud.gateway.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 路由工具类
 *
 * @author Houtaroy
 */
public abstract class RouteUtils {

  @Deprecated
  private static final Yaml YAML = new Yaml();
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

  @Deprecated
  @SuppressWarnings("unchecked")
  public static List<PredicateDefinition> yaml2PredicateDefinitions(String yaml) {
    List<PredicateDefinition> result = new ArrayList<>();
    if (StringUtils.hasText(yaml)) {
      List<Map<String, Object>> configs = YAML.load(yaml);
      result.addAll(configs.stream().map(config -> {
        PredicateDefinition definition = new PredicateDefinition();
        definition.setName(config.get("name").toString());
        definition.setArgs(yaml2DefinitionArgs((Map<String, Object>) config.get("args")));
        return definition;
      }).toList());
    }
    return result;
  }

  @Deprecated
  @SuppressWarnings("unchecked")
  public static List<FilterDefinition> yaml2FilterDefinitions(String yaml) {
    List<FilterDefinition> result = new ArrayList<>();
    if (StringUtils.hasText(yaml)) {
      List<Map<String, Object>> configs = YAML.load(yaml);
      result.addAll(configs.stream().map(config -> {
        FilterDefinition definition = new FilterDefinition();
        definition.setName(config.get("name").toString());
        definition.setArgs(yaml2DefinitionArgs((Map<String, Object>) config.get("args")));
        return definition;
      }).toList());
    }
    return result;
  }

  @Deprecated
  private static Map<String, String> yaml2DefinitionArgs(Map<String, Object> args) {
    if (args == null) {
      return new HashMap<>();
    }
    Map<String, String> result = new HashMap<>(args.size());
    args.forEach((key, value) -> result.put(key, value.toString()));
    return result;
  }

  @Deprecated
  public static Map<String, Object> yaml2Metadata(String yaml) {
    if (!StringUtils.hasText(yaml)) {
      return new HashMap<>();
    }
    return YAML.load(yaml);
  }
}
