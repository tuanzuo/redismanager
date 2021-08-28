package com.tz.redismanager.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>redis服务器信息</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-08-28 18:45
 **/
@Getter
@Setter
public class RedisServerInfoVO {

    /**
     * Redis服务器信息
     */
    private Object server;
    /**
     * 已连接客户端信息
     */
    private Object clients;
    /**
     * 内存信息
     */
    private Object memory;
    /**
     * RDB 和 AOF 的相关信息
     */
    private Object persistence;
    /**
     * 统计信息
     */
    private Object stats;
    /**
     * 主/从复制信息
     */
    private Object replication;
    /**
     * cpu
     */
    private Object cpu;
    /**
     * Redis命令统计信息
     */
    private Object commandstats;
    /**
     * 集群信息
     */
    private Object cluster;
    /**
     * 数据库相关的统计信息
     */
    private Object keyspace;
}
