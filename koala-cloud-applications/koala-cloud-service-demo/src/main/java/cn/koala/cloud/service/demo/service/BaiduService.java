package cn.koala.cloud.service.demo.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 百度服务
 *
 * @author Houtaroy
 */
@FeignClient(name = "baidu-service", url = "https://www.baidu.com")
public interface BaiduService {

  @GetMapping("/")
  String index();
}
