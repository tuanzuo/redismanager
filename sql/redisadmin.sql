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
