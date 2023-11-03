package cn.koala.cloud.gateway.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 加密体
 *
 * <p>data为加密后的报文
 *
 * @author Houtaroy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncryptBody {

  private String data;
}
