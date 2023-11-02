package cn.koala.cloud.gateway;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * 接口响应日志
 *
 * @author Houtaroy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("k_api_response_log")
public class ApiResponseLog implements Persistable<String> {

  @Id
  private String id;

  private Integer responseCode;

  private String responseHeaders;

  private String responseCookies;

  private String responseBody;

  private LocalDateTime responseTime;

  private LocalDateTime logTime;

  @Override
  public boolean isNew() {
    return true;
  }
}
