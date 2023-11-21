package cn.koala.cloud.service.demo.api;

import cn.koala.cloud.service.demo.service.BaiduService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 示例服务接口
 *
 * @author Houtaroy
 */
@RestController
@SecurityRequirement(name = "spring-security")
@RequiredArgsConstructor
public class ServiceDemoApi {

  private final BaiduService baiduService;

  @GetMapping("{id}")
  public String test(@PathVariable("id") Long id) {
    return id.toString();
  }

  @GetMapping("baidu")
  public String baidu() {
    return baiduService.index();
  }

  @PostMapping("post")
  public ResponseEntity<Map<String, Object>> post(@RequestBody Map<String, Object> body) {
    return ResponseEntity.ok(body);
  }
}
