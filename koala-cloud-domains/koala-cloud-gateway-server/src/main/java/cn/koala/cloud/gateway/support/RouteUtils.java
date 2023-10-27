package cn.koala.cloud.gateway.support;

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

  private static final Yaml YAML = new Yaml();

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

  private static Map<String, String> yaml2DefinitionArgs(Map<String, Object> args) {
    if (args == null) {
      return new HashMap<>();
    }
    Map<String, String> result = new HashMap<>(args.size());
    args.forEach((key, value) -> result.put(key, value.toString()));
    return result;
  }

  public static Map<String, Object> yaml2Metadata(String yaml) {
    if (!StringUtils.hasText(yaml)) {
      return new HashMap<>();
    }
    return YAML.load(yaml);
  }
}
