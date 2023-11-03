package cn.koala.cloud.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * 资源
 *
 * @author Houtaroy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("k_resource")
public class Resource {

  @Id
  private Long id;

  private Integer type;

  private String code;

  private String name;

  private String description;

  private Long createdBy;

  private LocalDateTime createdTime;

  private Long lastModifiedBy;

  private LocalDateTime lastModifiedTime;
}
