DROP DATABASE IF EXISTS `chat_web`;

CREATE DATABASE `chat_web` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

USE `chat_web`;

-- ----------------------------
-- Table structure for chat_customer
-- ----------------------------
DROP TABLE IF EXISTS `chat_customer`;
CREATE TABLE `chat_customer` (
    `id` bigint NOT NULL,
    `username` varchar(64) NOT NULL COMMENT '用户名',
    `password` varchar(255) NOT NULL COMMENT '密码',
    `salt` varchar(255) DEFAULT NULL COMMENT '随机盐',
    `phone` varchar(20) DEFAULT NULL COMMENT '简介',
    `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
    `about` varchar(255) DEFAULT NULL COMMENT '简介',
    `lock_flag` char(1) DEFAULT '0' COMMENT '0-正常，9-锁定',
    `del_flag` char(1) DEFAULT '0' COMMENT '0-正常，1-删除',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '修改时间',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
    `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    KEY `customer_idx1_username` (`username`),
    KEY `customer_idx2_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='聊天用户表';

-- ----------------------------
-- Table structure for chat_message
-- ----------------------------
DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE `chat_message` (
    `id` bigint NOT NULL,
    `sender_id` bigint NOT NULL COMMENT '发送者ID',
    `receiver_id` bigint NOT NULL COMMENT '接收者ID',
    `seq_num` bigint NOT NULL COMMENT '序号',
    `content` varchar(5000) NOT NULL COMMENT '消息内容',
    `content_type` int NOT NULL COMMENT '消息类型：1-text, 2-emoji, 3-image, 4-audio, 5-video',
    `content_subtype` int NOT NULL COMMENT '消息子类型',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '修改时间',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
    `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    KEY `message_idx1_receiver` (`receiver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='聊天消息表';

-- ----------------------------
-- Table structure for chat_friend
-- ----------------------------
DROP TABLE IF EXISTS `chat_friend`;
CREATE TABLE `chat_friend` (
    `id` bigint NOT NULL,
    `user_id` bigint NOT NULL COMMENT '用户id',
    `friend_id` bigint NOT NULL COMMENT '朋友id',
    `remark` varchar(256) NOT NULL COMMENT '备注',
    `request_status` int NOT NULL COMMENT '好友请求状态',
    `del_flag` char(1) DEFAULT '0' COMMENT '0-正常，1-删除',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '修改时间',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
    `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    KEY `friend_idx1_from_to` (`user_id`, `friend_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='朋友关系表';

-- ----------------------------
-- Table structure for chat_community
-- ----------------------------
DROP TABLE IF EXISTS `chat_community`;
CREATE TABLE `chat_community` (
    `id` bigint NOT NULL,
    `community_name` varchar(64) NOT NULL COMMENT '群聊名',
    `avatar` varchar(255) DEFAULT NULL COMMENT '群头像',
    `description` varchar(1000) NOT NULL COMMENT '群描述',
    `admin_id` bigint NOT NULL COMMENT '管理员ID',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '修改时间',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
    `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='群聊表';

-- ----------------------------
-- Table structure for chat_community_member
-- ----------------------------
DROP TABLE IF EXISTS `chat_community_member`;
CREATE TABLE `chat_community_member` (
    `id` bigint NOT NULL,
    `community_id` bigint NOT NULL COMMENT '群聊id',
    `member_id` bigint NOT NULL COMMENT '用户id',
    `nickname` varchar(64) DEFAULT NULL COMMENT '群内昵称',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '修改时间',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
    `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `community_member_idx1_community_member` (`community_id`, `member_id`),
    KEY `community_member_idx2_member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='聊天群成员';

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
   `id` bigint NOT NULL,
   `type` char(1) COLLATE utf8mb4_bin DEFAULT '1' COMMENT '日志类型',
   `title` varchar(255) COLLATE utf8mb4_bin DEFAULT '' COMMENT '日志标题',
   `service_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '服务ID',
   `remote_addr` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '操作IP地址',
   `user_agent` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户代理',
   `request_uri` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '请求URI',
   `method` varchar(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '操作方式',
   `params` text COLLATE utf8mb4_bin COMMENT '操作提交的数据',
   `time` bigint DEFAULT NULL COMMENT '执行时间',
   `del_flag` char(1) COLLATE utf8mb4_bin DEFAULT '0' COMMENT '删除标记',
   `exception` text COLLATE utf8mb4_bin COMMENT '异常信息',
   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
   `create_by` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人',
   `update_by` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '更新人',
   PRIMARY KEY (`id`),
   KEY `sys_log_create_by` (`create_by`),
   KEY `sys_log_request_uri` (`request_uri`),
   KEY `sys_log_type` (`type`),
   KEY `sys_log_create_date` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='日志表';

-- ----------------------------
-- Table structure for sys_oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth_client_details`;
CREATE TABLE `sys_oauth_client_details` (
    `client_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '客户端ID',
    `resource_ids` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '资源列表',
    `client_secret` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '客户端密钥',
    `scope` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '域',
    `authorized_grant_types` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '认证类型',
    `web_server_redirect_uri` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '重定向地址',
    `authorities` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '角色列表',
    `access_token_validity` int DEFAULT NULL COMMENT 'token 有效期',
    `refresh_token_validity` int DEFAULT NULL COMMENT '刷新令牌有效期',
    `additional_information` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '令牌扩展字段JSON',
    `autoapprove` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '是否自动放行',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人',
    `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='终端信息表';

-- ----------------------------
-- Records of sys_oauth_client_details
-- ----------------------------
BEGIN;
INSERT INTO `sys_oauth_client_details` VALUES ('app', NULL, 'app', 'server', 'app,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` VALUES ('chat',NULL,'chat','server','password,app,refresh_token,authorization_code,client_credentials','http://localhost:4040/sso1/login,http://localhost:4041/sso1/login',NULL,NULL,NULL,NULL,'true',NULL,NULL,NULL,NULL);
INSERT INTO `sys_oauth_client_details` VALUES ('client', NULL, 'client', 'server', 'client_credentials', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` VALUES ('daemon', NULL, 'daemon', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` VALUES ('gen', NULL, 'gen', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` VALUES ('pig', NULL, 'pig', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` VALUES ('open', NULL, 'open', 'server', 'password,app,refresh_token,authorization_code,client_credentials', 'http://localhost:4040/sso1/login,http://localhost:4041/sso1/login', NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` VALUES ('test', NULL, 'test', 'server', 'password,app,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;