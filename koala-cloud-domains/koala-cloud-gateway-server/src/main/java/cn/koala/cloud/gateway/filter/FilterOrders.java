package cn.koala.cloud.gateway.filter;

/**
 * 过滤器排序
 *
 * @author Houtaroy
 */
public interface FilterOrders {

  int CACHE_REQUEST_BODY_STRING = 100;
  int REGISTERED_CLIENT_ATTRIBUTE = 200;
  int RESOURCE_ATTRIBUTE = 300;
  int API_ATTRIBUTE = 400;
  int API_AUTHORIZATION_ATTRIBUTE = 500;
  int API_REQUEST_LOG = 600;
  int DECRYPT_REQUEST = 700;
  int ENCRYPT_RESPONSE = -900;
  int API_RESPONSE_LOG = -1000;

  int ACCESS_TOKEN = 1000;
  int API_AUTHORIZATION = 1100;
  int API_AUTHORIZATION_IP = 1200;
  int API_AUTHORIZATION_QUOTA = 1300;
}
