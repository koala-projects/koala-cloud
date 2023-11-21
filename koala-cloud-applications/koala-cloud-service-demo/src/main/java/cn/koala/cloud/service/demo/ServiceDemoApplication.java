package cn.koala.cloud.service.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 示例服务应用
 *
 * @author Houtaroy
 */
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class ServiceDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServiceDemoApplication.class, args);
  }
}
