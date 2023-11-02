package cn.koala.cloud.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 网关配置属性
 *
 * @author Houtaroy
 */
@Data
@ConfigurationProperties(prefix = "koala.cloud.gateway")
public class GatewayProperties {

  private boolean logging = true;
}
