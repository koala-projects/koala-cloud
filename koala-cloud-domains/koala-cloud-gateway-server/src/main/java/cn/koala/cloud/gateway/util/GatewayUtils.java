package cn.koala.cloud.gateway.util;

import cn.koala.cloud.gateway.model.Api;
import cn.koala.cloud.gateway.model.RegisteredClient;
import cn.koala.persist.domain.YesNo;

/**
 * TODO: 修改类描述
 *
 * @author Houtaroy
 */
public abstract class GatewayUtils {

  public static boolean isPlain(Api api, RegisteredClient client) {
    return (api == null || api.getIsSupportEncrypt() != YesNo.YES.getValue())
      || (client == null || !client.isEncrypted());
  }
}
