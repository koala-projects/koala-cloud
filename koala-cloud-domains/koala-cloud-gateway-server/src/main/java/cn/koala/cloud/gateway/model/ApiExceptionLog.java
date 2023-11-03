package cn.koala.cloud.gateway.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * 接口异常日志
 *
 * @author Houtaroy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("k_api_exception_log")
public class ApiExceptionLog {

  @Id
  private String id;

  private String message;

  private LocalDateTime logTime;
}
