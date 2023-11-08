package cn.koala.cloud.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 接口文档
 *
 * @author Houtaroy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("k_api_document")
public class ApiDocument {

  @Id
  private Long id;

  private Long resourceId;

  private String code;

  private String name;

  private String version;

  private String uri;

  private Integer isEnabled;

  private Integer isDeleted;
}
