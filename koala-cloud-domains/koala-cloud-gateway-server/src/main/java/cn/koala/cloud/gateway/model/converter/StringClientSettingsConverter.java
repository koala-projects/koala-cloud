package cn.koala.cloud.gateway.model.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import java.util.Map;

/**
 * Map转换器
 *
 * @author Houtaroy
 */
@RequiredArgsConstructor
public class StringClientSettingsConverter implements Converter<String, ClientSettings> {
  private static final TypeReference<Map<String, Object>> TYPE = new TypeReference<>() {
  };

  private final ObjectMapper objectMapper;

  @Override
  public ClientSettings convert(@NonNull String source) {
    try {
      Map<String, Object> settings = objectMapper.readValue(source, TYPE);
      return ClientSettings.withSettings(settings).build();
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }
}
