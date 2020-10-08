CREATE DATABASE `redisadmin`CHARACTER SET utf8 COLLATE utf8_bin;

CREATE TABLE `t_redis_config` (
  `id` varchar(32) collate utf8_bin NOT NULL COMMENT '主键',
  `name` varchar(1024) collate utf8_bin default NULL COMMENT '名称',
  `type` int(2) default '1' COMMENT '类型[1=单机,2=集群]',
  `address` varchar(1024) collate utf8_bin default NULL COMMENT '地址',
  `password` varchar(2048) collate utf8_bin default NULL COMMENT '密码',
  `ser_code` text collate utf8_bin COMMENT '序列化代码',
  `note` varchar(2048) collate utf8_bin default NULL COMMENT '备注',
  `creater` varchar(32) collate utf8_bin default NULL COMMENT '创建人',
  `create_time` datetime default NULL COMMENT '创建时间',
  `updater` varchar(32) collate utf8_bin default NULL COMMENT '修改人',
  `update_time` datetime default NULL COMMENT '修改时间',
  `if_del` int(2) default NULL COMMENT '是否删除[1=是,0=否]',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `t_role` (
  `id` int(11) NOT NULL auto_increment COMMENT '主键',
  `name` varchar(32) collate utf8_bin NOT NULL COMMENT '角色名称',
  `code` varchar(100) collate utf8_bin NOT NULL COMMENT '角色编码',
  `status` int(2) NOT NULL default '1' COMMENT '状态[1=启用,0=禁用]',
  `note` varchar(200) collate utf8_bin default NULL COMMENT '备注',
  `creater` varchar(32) collate utf8_bin default NULL COMMENT '创建人',
  `create_time` datetime default NULL COMMENT '创建时间',
  `updater` varchar(32) collate utf8_bin default NULL COMMENT '修改人',
  `update_time` datetime default NULL COMMENT '修改时间',
  `if_del` int(2) NOT NULL default '0' COMMENT '是否删除[1=是,0=否]',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `unique_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色表';

insert  into `t_role`(`id`,`name`,`code`,`status`,`note`,`creater`,`create_time`,`updater`,`update_time`,`if_del`)
values
(1,'超级管理员','superadmin',1,'超级管理人员','superadmin',now(),'superadmin',now(),0),
(2,'普通管理员','admin',1,'普通管理人员','superadmin',now(),'superadmin',now(),0),
(3,'测试','test',1,'测试人员','superadmin',now(),'superadmin',now(),0),
(4,'开发','develop',1,'开发人员','superadmin',now(),'superadmin',now(),0);


CREATE TABLE `t_user` (
  `id` int(11) NOT NULL auto_increment COMMENT '主键',
  `name` varchar(32) collate utf8_bin NOT NULL COMMENT '用户名',
  `pwd` varchar(2048) collate utf8_bin NOT NULL COMMENT '密码',
  `status` int(2) NOT NULL default '1' COMMENT '状态[1=启用,0=禁用]',
  `note` varchar(200) collate utf8_bin default NULL COMMENT '备注',
  `creater` varchar(32) collate utf8_bin default NULL COMMENT '创建人',
  `create_time` datetime default NULL COMMENT '创建时间',
  `updater` varchar(32) collate utf8_bin default NULL COMMENT '修改人',
  `update_time` datetime default NULL COMMENT '修改时间',
  `if_del` int(2) NOT NULL default '0' COMMENT '是否删除[1=是,0=否]',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `unique_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户表';

insert  into `t_user`(`id`,`name`,`pwd`,`status`,`note`,`creater`,`create_time`,`updater`,`update_time`,`if_del`) values (1,'superadmin','7e7e3165a2ab371ad00102bbf440d9c3',1,'有人梦我与前尘','superadmin','2020-10-07 12:17:07','superadmin','2020-10-07 12:17:07',0),(2,'admin','1c7b1e2c168f93cf17b4fe288f2b5fb7',1,'有人与我立黄昏','admin','2020-10-07 12:19:08','admin','2020-10-07 12:19:08',0),(3,'develop','53da58e3c24e14e4abb1a59ef8e9efaf',1,'有人与我把酒分','develop','2020-10-07 12:19:51','develop','2020-10-07 12:19:51',0),(4,'test','4204ae17473c367b30419ecc63410ea4',1,'有人与我立黄昏','test','2020-10-07 12:20:06','test','2020-10-07 12:20:06',0);

CREATE TABLE `t_user_role_relation` (
  `id` int(11) NOT NULL auto_increment COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户Id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `creater` varchar(32) collate utf8_bin default NULL COMMENT '创建人',
  `create_time` datetime default NULL COMMENT '创建时间',
  `updater` varchar(32) collate utf8_bin default NULL COMMENT '修改人',
  `update_time` datetime default NULL COMMENT '修改时间',
  `if_del` int(2) default NULL COMMENT '是否删除[1=是,0=否]',
  PRIMARY KEY  (`id`),
  KEY `index_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户角色关系表';

insert  into `t_user_role_relation`(`id`,`user_id`,`role_id`,`creater`,`create_time`,`updater`,`update_time`,`if_del`) values (1,1,1,'superadmin','2020-10-07 12:17:08','superadmin','2020-10-07 12:17:08',0),(2,1,2,'superadmin','2020-10-07 12:17:08','superadmin','2020-10-07 12:17:08',0),(3,1,3,'superadmin','2020-10-07 12:17:08','superadmin','2020-10-07 12:17:08',0),(4,1,4,'superadmin','2020-10-07 12:17:08','superadmin','2020-10-07 12:17:08',0),(5,2,2,'admin','2020-10-07 12:19:08','admin','2020-10-07 12:19:08',0),(6,2,3,'admin','2020-10-07 12:19:08','admin','2020-10-07 12:19:08',0),(7,2,4,'admin','2020-10-07 12:19:08','admin','2020-10-07 12:19:08',0),(8,3,2,'develop','2020-10-07 12:19:51','develop','2020-10-07 12:19:51',0),(9,3,3,'develop','2020-10-07 12:19:51','develop','2020-10-07 12:19:51',0),(10,3,4,'develop','2020-10-07 12:19:51','develop','2020-10-07 12:19:51',0),(11,4,2,'test','2020-10-07 12:20:06','test','2020-10-07 12:20:06',0),(12,4,3,'test','2020-10-07 12:20:06','test','2020-10-07 12:20:06',0),(13,4,4,'test','2020-10-07 12:20:06','test','2020-10-07 12:20:06',0);