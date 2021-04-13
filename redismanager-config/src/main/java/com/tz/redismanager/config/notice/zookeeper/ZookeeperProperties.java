package com.tz.redismanager.config.notice.zookeeper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-09 20:42
 **/
@Configuration
@ConfigurationProperties(prefix = "rm.config.zookeeper")
@Getter
@Setter
public class ZookeeperProperties {

    /**
     * 连接地址
     */
    private String connectString = "127.0.0.1:2181";

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

}
