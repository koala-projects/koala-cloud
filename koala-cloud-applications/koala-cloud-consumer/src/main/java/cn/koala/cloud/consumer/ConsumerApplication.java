package cn.koala.cloud.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 消费者应用
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

  @RequestMapping("/test/{id}")
  public String test(@PathVariable("id") Long id) {
    return this.testService.test();
  }

  @PostMapping("/test/post")
  public ResponseEntity<Map<String, Object>> post(@RequestBody Map<String, Object> body) {
    return ResponseEntity.ok(Map.of("data", "test"));
  }

  public static void main(String[] args) {
    SpringApplication.run(ConsumerApplication.class, args);
  }
}
