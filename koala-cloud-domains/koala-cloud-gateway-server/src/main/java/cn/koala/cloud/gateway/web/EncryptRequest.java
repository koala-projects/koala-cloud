package cn.koala.cloud.gateway.web;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口加密请求
 *
 * <p>data为客户端加密后的报文
 *
 * @author Houtaroy
 */
@Data
@NoArgsConstructor
public class EncryptRequest {

  private String data;
}
