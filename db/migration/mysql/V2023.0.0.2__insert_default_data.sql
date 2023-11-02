-- 默认资源
INSERT INTO k_resource
(id, `type`, code, name, description, sort_index, is_enabled, is_systemic, is_deleted, created_by, created_time, last_modified_by, last_modified_time, deleted_by, deleted_time)
VALUES(1, '1', 'koala-cloud-authorization-server', '认证授权服务资源', NULL, 1, 1, 1, 0, '1', '2023-11-02 14:57:04', NULL, NULL, NULL, NULL);
INSERT INTO k_resource
(id, `type`, code, name, description, sort_index, is_enabled, is_systemic, is_deleted, created_by, created_time, last_modified_by, last_modified_time, deleted_by, deleted_time)
VALUES(2, '1', 'koala-cloud-provider', '服务提供者资源', NULL, 2, 1, 1, 0, '1', '2023-11-02 14:58:29', NULL, NULL, NULL, NULL);
INSERT INTO k_resource
(id, `type`, code, name, description, sort_index, is_enabled, is_systemic, is_deleted, created_by, created_time, last_modified_by, last_modified_time, deleted_by, deleted_time)
VALUES(3, '1', 'koala-cloud-consumer', '服务消费者资源', NULL, 3, 1, 1, 0, '1', '2023-11-02 14:59:27', NULL, NULL, NULL, NULL);

-- 默认接口
INSERT INTO k_api
(id, resource_id, code, name, description, `path`, `method`, is_permissible, sort_index, is_enabled, is_systemic, is_deleted, created_by, created_time, last_modified_by, last_modified_time, deleted_by, deleted_time)
VALUES(1, 3, 'koala-cloud-consumer-test-get', '消费者GET测试接口', '消费者GET测试接口', '/test/{id}', 'GET', 0, 0, 1, 0, 0, '1', '2023-10-26 14:06:56', NULL, NULL, NULL, NULL);
INSERT INTO k_api
(id, resource_id, code, name, description, `path`, `method`, is_permissible, sort_index, is_enabled, is_systemic, is_deleted, created_by, created_time, last_modified_by, last_modified_time, deleted_by, deleted_time)
VALUES(2, 3, 'koala-cloud-consumer-test-post', '消费者POST测试接口', '消费者POST测试接口', '/test/post', 'POST', 1, 0, 1, 0, 0, '1', '2023-10-26 14:06:56', NULL, NULL, NULL, NULL);

-- 默认接口授权
INSERT INTO k_api_authorization
(id, client_id, api_id, expires_at, quota, ips, is_deleted, created_by, created_time, last_modified_by, last_modified_time, deleted_by, deleted_time)
VALUES(1, 'koala-admin', 1, '2023-12-31 14:59:11', 100, '127.0.0.1', 0, '1', '2023-10-26 14:59:22', NULL, NULL, NULL, NULL);

-- 默认路由
INSERT INTO k_route
(id, name, resource_id, uri, predicates, filters, metadata, sort_index, is_enabled, is_systemic, is_deleted, created_by, created_time, last_modified_by, last_modified_time, deleted_by, deleted_time)
VALUES('koala-cloud-authorization-server', '认证授权服务路由', 1, 'lb://koala-cloud-authorization-server', '[{"args": {"pattern": "/oauth2/**"}, "name": "Path"}]', NULL, NULL, 0, 1, 1, 0, '1', '2023-10-27 14:59:56', NULL, NULL, NULL, NULL);
INSERT INTO k_route
(id, name, resource_id, uri, predicates, filters, metadata, sort_index, is_enabled, is_systemic, is_deleted, created_by, created_time, last_modified_by, last_modified_time, deleted_by, deleted_time)
VALUES('koala-cloud-consumer', '考拉微服务消费者路由', 3, 'lb://koala-cloud-consumer', '[{"args": {"pattern": "/consumer/**"}, "name": "Path"}]', '[{"args": {"parts": "1"}, "name": "StripPrefix"}, {"name": "SecureHeaders"}, {"name": "ApiAuthorization"}, {"name": "ApiIp"}]', NULL, 0, 1, 1, 0, '1', '2023-10-27 15:19:26', NULL, NULL, NULL, NULL);
INSERT INTO k_route
(id, name, resource_id, uri, predicates, filters, metadata, sort_index, is_enabled, is_systemic, is_deleted, created_by, created_time, last_modified_by, last_modified_time, deleted_by, deleted_time)
VALUES('koala-cloud-provider', '考拉微服务提供者路由', 2, 'lb://koala-cloud-provider', '[{"args": {"pattern": "/provider/**"}, "name": "Path"}]', '[{"args": {"parts": "1"}, "name": "StripPrefix"}]', NULL, 0, 1, 1, 0, '1', '2023-10-27 15:18:43', NULL, NULL, NULL, NULL);