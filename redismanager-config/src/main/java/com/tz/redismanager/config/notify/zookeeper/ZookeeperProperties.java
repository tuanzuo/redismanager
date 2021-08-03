package com.tz.redismanager.config.notify.zookeeper;

import com.tz.redismanager.config.constant.ConstInterface;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>zookeeper配置properties</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-09 20:42
 **/
@ConfigurationProperties(prefix = "rm.config.zookeeper")
@Getter
@Setter
public class ZookeeperProperties {

    private String parentPath = ConstInterface.Zookeeper.BASE_PATH;

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
