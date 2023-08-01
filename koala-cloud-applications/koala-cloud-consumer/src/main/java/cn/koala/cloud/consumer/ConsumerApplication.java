package cn.koala.cloud.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO: 修改类描述
 *
 * @author Houtaroy
 */
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@RestController
@RequiredArgsConstructor
public class ConsumerApplication {

  @FeignClient(name = "koala-cloud-provider")
  public interface TestService {

    @GetMapping(value = "/test")
    String test();
  }

  private final TestService testService;

  @RequestMapping("/test")
  public String test() {
    return this.testService.test();
  }

  public static void main(String[] args) {
    SpringApplication.run(ConsumerApplication.class, args);
  }
}
