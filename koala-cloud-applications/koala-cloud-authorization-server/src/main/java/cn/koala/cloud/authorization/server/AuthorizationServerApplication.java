package cn.koala.cloud.authorization.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 消费者应用
 *
 * @author Houtaroy
 */
@EnableDiscoveryClient
@SpringBootApplication
public class AuthorizationServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(AuthorizationServerApplication.class, args);
  }
}
