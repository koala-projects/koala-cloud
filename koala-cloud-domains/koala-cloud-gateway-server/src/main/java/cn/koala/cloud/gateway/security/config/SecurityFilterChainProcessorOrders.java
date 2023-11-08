package cn.koala.cloud.gateway.security.config;

/**
 * 安全过滤器链处理器排序
 *
 * @author Houtaroy
 */
public interface SecurityFilterChainProcessorOrders {

  int SWAGGER_PERMIT_ALL = 100;
  int API_DOCUMENT_PERMIT_ALL = 200;
  int OAUTH2 = 10000;
}
