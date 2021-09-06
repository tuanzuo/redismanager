CREATE TABLE `t_redis_config` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(1024) collate utf8_bin default NULL COMMENT '名称',
  `is_public` tinyint(2) DEFAULT '1' COMMENT '是否公开[1=是,0=否]',
  `type` tinyint(2) default '1' COMMENT '类型[1=单机,2=集群]',
  `address` varchar(1024) collate utf8_bin default NULL COMMENT '地址',
  `password` varchar(2048) collate utf8_bin default NULL COMMENT '密码',
  `ser_code` text collate utf8_bin COMMENT '序列化代码',
  `note` varchar(2048) collate utf8_bin default NULL COMMENT '备注',
  `creater` varchar(32) collate utf8_bin default NULL COMMENT '创建人',
  `create_time` datetime default NULL COMMENT '创建时间',
  `updater` varchar(32) collate utf8_bin default NULL COMMENT '修改人',
  `update_time` datetime default NULL COMMENT '修改时间',
  `if_del` tinyint(2) NOT NULL default '0' COMMENT '是否删除[1=是,0=否]',
  PRIMARY KEY  (`id`),
  KEY `idx_name` (`name`(50)),
  KEY `idx_address` (`address`(50))
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='redis连接配置表';

CREATE TABLE `t_redis_config_ext` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `rconfig_id` bigint(20) NOT NULL COMMENT '关联的配置id',
  `ext_key` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '扩展key',
  `ext_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '扩展名称',
  `ext_value` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '扩展value',
  `note` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `creater` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updater` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `if_del` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否删除[1=是,0=否]',
  PRIMARY KEY (`id`),
  KEY `idx_rconfig_id` (`rconfig_id`),
  KEY `idx_ext_key` (`ext_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='redis连接配置扩展表';

CREATE TABLE `t_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) collate utf8_bin NOT NULL COMMENT '角色名称',
  `code` varchar(100) collate utf8_bin NOT NULL COMMENT '角色编码',
  `status` tinyint(2) NOT NULL default '1' COMMENT '状态[1=启用,0=禁用]',
  `note` varchar(200) collate utf8_bin default NULL COMMENT '备注',
  `creater` varchar(32) collate utf8_bin default NULL COMMENT '创建人',
  `create_time` datetime default NULL COMMENT '创建时间',
  `updater` varchar(32) collate utf8_bin default NULL COMMENT '修改人',
  `update_time` datetime default NULL COMMENT '修改时间',
  `if_del` tinyint(2) NOT NULL default '0' COMMENT '是否删除[1=是,0=否]',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `uidx_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色表';

insert  into `t_role`(`id`,`name`,`code`,`status`,`note`,`creater`,`create_time`,`updater`,`update_time`,`if_del`)
values
(1,'超级管理员','superadmin',1,'超级管理人员','superadmin',now(),'superadmin',now(),0),
(2,'普通管理员','admin',1,'普通管理人员','superadmin',now(),'superadmin',now(),0),
(3,'测试','test',1,'测试人员','superadmin',now(),'superadmin',now(),0),
(4,'开发','develop',1,'开发人员','superadmin',now(),'superadmin',now(),0);


CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) collate utf8_bin NOT NULL COMMENT '用户名',
  `pwd` varchar(2048) collate utf8_bin NOT NULL COMMENT '密码',
  `status` tinyint(2) NOT NULL default '1' COMMENT '状态[1=启用,0=禁用]',
  `note` varchar(200) collate utf8_bin default NULL COMMENT '备注',
  `creater` varchar(32) collate utf8_bin default NULL COMMENT '创建人',
  `create_time` datetime default NULL COMMENT '创建时间',
  `updater` varchar(32) collate utf8_bin default NULL COMMENT '修改人',
  `update_time` datetime default NULL COMMENT '修改时间',
  `if_del` tinyint(2) NOT NULL default '0' COMMENT '是否删除[1=是,0=否]',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `uidx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户表';

insert  into `t_user`(`id`,`name`,`pwd`,`status`,`note`,`creater`,`create_time`,`updater`,`update_time`,`if_del`)
values
(1,'superadmin','7e7e3165a2ab371ad00102bbf440d9c3',1,'有人梦我与前尘','superadmin','2020-10-07 12:17:07','superadmin','2020-10-07 12:17:07',0),
(2,'admin','1c7b1e2c168f93cf17b4fe288f2b5fb7',1,'有人与我立黄昏','admin','2020-10-07 12:19:08','admin','2020-10-07 12:19:08',0),
(3,'develop','53da58e3c24e14e4abb1a59ef8e9efaf',1,'有人与我把酒分','develop','2020-10-07 12:19:51','develop','2020-10-07 12:19:51',0),
(4,'test','4204ae17473c367b30419ecc63410ea4',1,'有人与我立黄昏','test','2020-10-07 12:20:06','test','2020-10-07 12:20:06',0);

CREATE TABLE `t_user_role_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户Id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `creater` varchar(32) collate utf8_bin default NULL COMMENT '创建人',
  `create_time` datetime default NULL COMMENT '创建时间',
  `updater` varchar(32) collate utf8_bin default NULL COMMENT '修改人',
  `update_time` datetime default NULL COMMENT '修改时间',
  `if_del` tinyint(2) NOT NULL default '0' COMMENT '是否删除[1=是,0=否]',
  PRIMARY KEY  (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户角色关系表';

insert  into `t_user_role_relation`(`id`,`user_id`,`role_id`,`creater`,`create_time`,`updater`,`update_time`,`if_del`)
values
(1,1,1,'superadmin','2020-10-07 12:17:08','superadmin','2020-10-07 12:17:08',0),
(2,1,2,'superadmin','2020-10-07 12:17:08','superadmin','2020-10-07 12:17:08',0),
(3,1,3,'superadmin','2020-10-07 12:17:08','superadmin','2020-10-07 12:17:08',0),
(4,1,4,'superadmin','2020-10-07 12:17:08','superadmin','2020-10-07 12:17:08',0),
(5,2,2,'admin','2020-10-07 12:19:08','admin','2020-10-07 12:19:08',0),
(6,2,3,'admin','2020-10-07 12:19:08','admin','2020-10-07 12:19:08',0),
(7,2,4,'admin','2020-10-07 12:19:08','admin','2020-10-07 12:19:08',0),
(8,3,2,'develop','2020-10-07 12:19:51','develop','2020-10-07 12:19:51',0),
(9,3,3,'develop','2020-10-07 12:19:51','develop','2020-10-07 12:19:51',0),
(10,3,4,'develop','2020-10-07 12:19:51','develop','2020-10-07 12:19:51',0),
(11,4,2,'test','2020-10-07 12:20:06','test','2020-10-07 12:20:06',0),
(12,4,3,'test','2020-10-07 12:20:06','test','2020-10-07 12:20:06',0),
(13,4,4,'test','2020-10-07 12:20:06','test','2020-10-07 12:20:06',0);

CREATE TABLE `t_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `service_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '服务名',
  `config_type` tinyint(3) NOT NULL COMMENT '配置类型[10=生效缓存配置,20=失效缓存配置,30=限流配置,40=token配置]',
  `config_key` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '配置key',
  `key_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '配置key名称',
  `content` text COLLATE utf8_bin NOT NULL COMMENT '配置内容JSON',
  `version` int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
  `note` varchar(300) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `creater` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updater` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `if_del` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否删除[1=是,0=否]',
  PRIMARY KEY (`id`),
  KEY `idx_service_name` (`service_name`),
  KEY `idx_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='配置表';

INSERT INTO `t_config` (`id`,`service_name`, `config_type`, `config_key`, `key_name`, `content`, `version`, `note`, `creater`, `create_time`, `updater`, `update_time`, `if_del`)
VALUES
(1,'redismanagerweb', 10, 'rm:analysis:rds:conf', 'redis连接配置分析页缓存', '{\"asyncRefresh\":false,\"key\":\"rm:analysis:rds:conf\",\"l1Cache\":{\"enable\":false,\"expireDuration\":2,\"expireStrategy\":\"EXPIRE_AFTER_ACCESS\",\"expireUnit\":\"HOURS\",\"initialCapacity\":100,\"maximumSize\":1000,\"recordStats\":true},\"l2Cache\":{\"enable\":false,\"expireDuration\":10,\"expireUnit\":\"MINUTES\"},\"name\":\"redis连接配置分析页缓存\"}', 10, NULL, 'sys', '2021-04-22 23:01:37', 'superadmin', '2021-08-07 23:05:43', 0),
(2,'redismanagerweb', 30, 'CONFIG_LIST_API', '查询配置列表请求限流', '{\"key\":\"CONFIG_LIST_API\",\"name\":\"查询配置列表请求限流\",\"permits\":1,\"qps\":200.0,\"timeout\":0,\"unit\":\"MICROSECONDS\"}', 12, NULL, 'superadmin', '2021-04-23 22:46:53', 'superadmin', '2021-08-07 22:52:10', 0),
(3,'redismanagerweb', 30, 'CAPTCHA_API', '验证码限流', '{\"key\":\"CAPTCHA_API\",\"name\":\"验证码限流\",\"permits\":1,\"qps\":100.0,\"timeout\":0,\"unit\":\"MICROSECONDS\"}', 20, NULL, 'superadmin', '2021-04-24 19:26:43', 'superadmin', '2021-08-07 22:53:52', 0);

CREATE TABLE `worker_node` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'auto increment id',
  `HOST_NAME` varchar(64) COLLATE utf8_bin NOT NULL COMMENT 'host name',
  `PORT` varchar(64) COLLATE utf8_bin NOT NULL COMMENT 'port',
  `TYPE` int(11) NOT NULL COMMENT 'node type: ACTUAL or CONTAINER',
  `LAUNCH_DATE` date NOT NULL COMMENT 'launch date',
  `MODIFIED` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modified time',
  /*`CREATED` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'created time',*/
  `CREATED` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'created time',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='DB WorkerID Assigner for UID Generator';