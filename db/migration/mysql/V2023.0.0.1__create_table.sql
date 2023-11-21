-- Authorization Server
-- OAuth2注册客户端表
CREATE TABLE oauth2_registered_client
(
  id                            varchar(100)                            NOT NULL,
  client_id                     varchar(100)                            NOT NULL,
  client_id_issued_at           timestamp     DEFAULT CURRENT_TIMESTAMP NOT NULL,
  client_secret                 varchar(200)  DEFAULT NULL,
  client_secret_expires_at      datetime      DEFAULT NULL,
  client_name                   varchar(200)                            NOT NULL,
  client_authentication_methods varchar(1000)                           NOT NULL,
  authorization_grant_types     varchar(1000)                           NOT NULL,
  redirect_uris                 varchar(1000) DEFAULT NULL,
  scopes                        varchar(1000)                           NOT NULL,
  client_settings               varchar(2000)                           NOT NULL,
  token_settings                varchar(2000)                           NOT NULL,
  PRIMARY KEY (id)
);

-- OAuth2授权表
CREATE TABLE oauth2_authorization_consent
(
  registered_client_id varchar(100)  NOT NULL,
  principal_name       varchar(200)  NOT NULL,
  authorities          varchar(1000) NOT NULL,
  PRIMARY KEY (registered_client_id, principal_name)
);

-- OAuth2授权信息表
CREATE TABLE oauth2_authorization
(
  id                            varchar(100) NOT NULL,
  registered_client_id          varchar(100) NOT NULL,
  principal_name                varchar(200) NOT NULL,
  authorization_grant_type      varchar(100) NOT NULL,
  authorized_scopes             varchar(1000) DEFAULT NULL,
  attributes                    blob          DEFAULT NULL,
  state                         varchar(500)  DEFAULT NULL,
  authorization_code_value      blob          DEFAULT NULL,
  authorization_code_issued_at  datetime      DEFAULT NULL,
  authorization_code_expires_at datetime      DEFAULT NULL,
  authorization_code_metadata   blob          DEFAULT NULL,
  access_token_value            blob          DEFAULT NULL,
  access_token_issued_at        datetime      DEFAULT NULL,
  access_token_expires_at       datetime      DEFAULT NULL,
  access_token_metadata         blob          DEFAULT NULL,
  access_token_type             varchar(100)  DEFAULT NULL,
  access_token_scopes           varchar(1000) DEFAULT NULL,
  oidc_id_token_value           blob          DEFAULT NULL,
  oidc_id_token_issued_at       datetime      DEFAULT NULL,
  oidc_id_token_expires_at      datetime      DEFAULT NULL,
  oidc_id_token_metadata        blob          DEFAULT NULL,
  refresh_token_value           blob          DEFAULT NULL,
  refresh_token_issued_at       datetime      DEFAULT NULL,
  refresh_token_expires_at      datetime      DEFAULT NULL,
  refresh_token_metadata        blob          DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE k_resource
(
  `id`                 INT          NOT NULL COMMENT '主键',
  `code`               VARCHAR(90)  NOT NULL COMMENT '资源代码',
  `name`               VARCHAR(90)  NOT NULL COMMENT '资源名称',
  `type`               VARCHAR(900) NOT NULL COMMENT '资源类型',
  `description`        VARCHAR(500) COMMENT '资源描述',
  `sort_index`         INT          NOT NULL DEFAULT 0 COMMENT '排序索引',
  `is_enabled`         INT          NOT NULL DEFAULT 1 COMMENT '是否启用',
  `is_systemic`        INT          NOT NULL DEFAULT 0 COMMENT '是否系统',
  `is_deleted`         INT          NOT NULL DEFAULT 0 COMMENT '是否删除',
  `created_by`         VARCHAR(32)  NOT NULL COMMENT '创建人ID',
  `created_time`       DATETIME     NOT NULL COMMENT '创建时间',
  `last_modified_by`   VARCHAR(32) COMMENT '最后更新人ID',
  `last_modified_time` DATETIME COMMENT '最后更新时间',
  `deleted_by`         VARCHAR(32) COMMENT '删除人ID',
  `deleted_time`       DATETIME COMMENT '删除时间',
  PRIMARY KEY (id)
) COMMENT = '资源表';

CREATE TABLE k_api
(
  `id`                 INT         NOT NULL AUTO_INCREMENT COMMENT '主键',
  `resource_id`        INT         NOT NULL COMMENT '资源id',
  `code`               VARCHAR(90) NOT NULL COMMENT '接口代码',
  `name`               VARCHAR(90) NOT NULL COMMENT '接口名称',
  `description`        VARCHAR(900) COMMENT '接口描述',
  `path`               VARCHAR(90) NOT NULL COMMENT '接口路径',
  `method`             VARCHAR(90) NOT NULL COMMENT '接口请求方式',
  `is_permissible`     INT         NOT NULL DEFAULT 0 COMMENT '是否许可的',
  `sort_index`         INT         NOT NULL DEFAULT 0 COMMENT '排序索引',
  `is_enabled`         INT         NOT NULL DEFAULT 1 COMMENT '是否启用',
  `is_systemic`        INT         NOT NULL DEFAULT 0 COMMENT '是否系统',
  `is_deleted`         INT         NOT NULL DEFAULT 0 COMMENT '是否删除',
  `created_by`         VARCHAR(32) NOT NULL COMMENT '创建人ID',
  `created_time`       DATETIME    NOT NULL COMMENT '创建时间',
  `last_modified_by`   VARCHAR(32) COMMENT '最后更新人ID',
  `last_modified_time` DATETIME COMMENT '最后更新时间',
  `deleted_by`         VARCHAR(32) COMMENT '删除人ID',
  `deleted_time`       DATETIME COMMENT '删除时间',
  PRIMARY KEY (id)
) COMMENT = '接口表';

CREATE TABLE k_api_authorization
(
  `id`                 INT         NOT NULL AUTO_INCREMENT COMMENT '主键',
  `client_id`          VARCHAR(90) NOT NULL COMMENT '客户端id',
  `api_id`             INT         NOT NULL COMMENT '接口id',
  `expires_at`         DATETIME    NOT NULL COMMENT '过期时间',
  `quota`              INT         NOT NULL COMMENT '调用额度',
  `ips`                VARCHAR(2000) COMMENT 'ip列表',
  `is_deleted`         INT         NOT NULL DEFAULT 0 COMMENT '是否删除',
  `created_by`         VARCHAR(32) NOT NULL COMMENT '创建人ID',
  `created_time`       DATETIME    NOT NULL COMMENT '创建时间',
  `last_modified_by`   VARCHAR(32) COMMENT '最后更新人ID',
  `last_modified_time` DATETIME COMMENT '最后更新时间',
  `deleted_by`         VARCHAR(32) COMMENT '删除人ID',
  `deleted_time`       DATETIME COMMENT '删除时间',
  PRIMARY KEY (id)
) COMMENT = '接口授权表';

CREATE TABLE k_route
(
  `id`                 VARCHAR(90) NOT NULL COMMENT '主键',
  `name`               VARCHAR(90) NOT NULL COMMENT '路由名称',
  `resource_id`        INT         NOT NULL COMMENT '资源id',
  `uri`                VARCHAR(90) NOT NULL COMMENT '资源标志符',
  `predicates`         JSON COMMENT '路由断言',
  `filters`            JSON COMMENT '路由过滤器',
  `metadata`           JSON COMMENT '路由元数据',
  `sort_index`         INT                  DEFAULT 0 COMMENT '排序索引',
  `is_enabled`         INT         NOT NULL DEFAULT 1 COMMENT '是否启用',
  `is_systemic`        INT         NOT NULL DEFAULT 0 COMMENT '是否系统',
  `is_deleted`         INT         NOT NULL DEFAULT 0 COMMENT '是否删除',
  `created_by`         VARCHAR(32) NOT NULL COMMENT '创建人ID',
  `created_time`       DATETIME    NOT NULL COMMENT '创建时间',
  `last_modified_by`   VARCHAR(32) COMMENT '最后更新人ID',
  `last_modified_time` DATETIME COMMENT '最后更新时间',
  `deleted_by`         VARCHAR(32) COMMENT '删除人ID',
  `deleted_time`       DATETIME COMMENT '删除时间',
  PRIMARY KEY (id)
) COMMENT = '路由表';

CREATE TABLE k_api_request_log
(
  `id`                   VARCHAR(36)   NOT NULL COMMENT '主键',
  `client`               JSON COMMENT '客户端信息',
  `route`                JSON          NOT NULL COMMENT '路由信息',
  `resource`             JSON COMMENT '资源信息',
  `api`                  JSON COMMENT '接口信息',
  `api_authorization`    JSON COMMENT '接口授权信息',
  `request_uri`          VARCHAR(2000) NOT NULL COMMENT '请求路径',
  `request_method`       VARCHAR(90)   NOT NULL COMMENT '请求方式',
  `request_query_params` JSON COMMENT '请求参数',
  `request_headers`      JSON          NOT NULL COMMENT '请求头',
  `request_token`        VARCHAR(2000) COMMENT '请求令牌',
  `request_cookies`      JSON COMMENT '请求cookies',
  `request_body`         TEXT COMMENT '请求体',
  `request_time`         DATETIME(3)   NOT NULL COMMENT '请求时间',
  `log_time`             DATETIME      NOT NULL COMMENT '日志时间',
  PRIMARY KEY (id)
) COMMENT = '接口请求日志表';

CREATE TABLE k_api_response_log
(
  `id`               VARCHAR(36) NOT NULL COMMENT '主键',
  `response_code`    INT         NOT NULL COMMENT '响应代码',
  `response_headers` JSON COMMENT '响应头',
  `response_cookies` JSON COMMENT '响应cookies',
  `response_body`    TEXT COMMENT '响应体',
  `response_time`    DATETIME(3) NOT NULL COMMENT '响应时间',
  `log_time`         DATETIME    NOT NULL COMMENT '日志时间',
  PRIMARY KEY (id)
) COMMENT = '接口响应日志表';

CREATE TABLE k_api_exception_log
(
  `id`       VARCHAR(36) NOT NULL COMMENT '主键',
  `message`  TEXT        NOT NULL COMMENT '异常信息',
  `log_time` DATETIME    NOT NULL COMMENT '日志时间',
  PRIMARY KEY (id)
) COMMENT = '接口异常日志表';
