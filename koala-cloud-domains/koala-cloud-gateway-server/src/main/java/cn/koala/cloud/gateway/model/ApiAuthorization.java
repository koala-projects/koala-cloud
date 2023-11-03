package cn.koala.cloud.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 接口授权实体类
 *
 * @author Houtaroy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("k_api_authorization")
public class ApiAuthorization {

  @Id
  private Long id;

  private String clientId;

  private Long apiId;

  private LocalDate expiresAt;

  private Integer quota;

  private List<String> ips;

  private Long createdBy;

  private LocalDateTime createdTime;

  private Long lastModifiedBy;

  private LocalDateTime lastModifiedTime;

  private Integer isDeleted;
}
