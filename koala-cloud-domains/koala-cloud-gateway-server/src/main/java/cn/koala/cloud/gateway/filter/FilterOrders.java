package cn.koala.cloud.gateway.filter;

/**
 * 过滤器排序
 *
 * @author Houtaroy
 */
public interface FilterOrders {

  int CACHE_REQUEST_BODY_STRING = 100;
  int REGISTERED_CLIENT = 200;
  int API_ATTRIBUTE = 300;
  int API_AUTHORIZATION_ATTRIBUTE = 400;
  int API_REQUEST_LOG = 500;
  int DECRYPT_REQUEST = 600;
  int ENCRYPT_RESPONSE = -900;
  int API_RESPONSE_LOG = -1000;

  int API_AUTHORIZATION = 1000;
  int API_IP = 1100;
}