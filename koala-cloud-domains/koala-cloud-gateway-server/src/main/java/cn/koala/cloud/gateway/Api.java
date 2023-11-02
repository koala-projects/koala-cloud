package cn.koala.cloud.gateway;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * 接口实体类
 *
 * @author Houtaroy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("k_api")
public class Api {

  @Id
  private Long id;

  private Long resourceId;

  private String code;

  private String name;

  private String description;

  private String path;

  private String method;

  private Integer isPermissible;

  private Integer isEnabled;

  private Long createdBy;

  private LocalDateTime createdTime;

  private Long lastModifiedBy;

  private LocalDateTime lastModifiedTime;

  private Integer isDeleted;
}
