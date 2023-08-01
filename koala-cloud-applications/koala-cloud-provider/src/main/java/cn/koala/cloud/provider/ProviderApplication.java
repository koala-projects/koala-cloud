package cn.koala.cloud.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO: 修改类描述
 *
 * @author Houtaroy
 */
@EnableDiscoveryClient
@RestController
@SpringBootApplication
public class ProviderApplication {

  @RequestMapping("/test")
  public String test() {
    return "test";
  }

  public static void main(String[] args) {
    SpringApplication.run(ProviderApplication.class, args);
  }
}
