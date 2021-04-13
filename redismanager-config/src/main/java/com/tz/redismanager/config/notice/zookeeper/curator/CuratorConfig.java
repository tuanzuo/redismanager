package com.tz.redismanager.config.notice.zookeeper.curator;

import com.tz.redismanager.config.notice.zookeeper.ZookeeperProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-08 22:41
 **/
@Configuration
public class CuratorConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String SCHEME_DIGEST = "digest";

    @Autowired
    private ZookeeperProperties zookeeperProperties;

    @Autowired
    private CuratorProperties curatorProperties;

    /**
     * Curator Apache
     * 提供访问Zookeeper的工具包，封装了这些低级别操作同时也提供一些高级服务，比如分布式锁、领导选取
     *
     * @return
     */
    @Bean
    public CuratorFramework curatorFramework(CuratorListener curatorListener) {
        // ExponentialBackoffRetry是种重连策略，每次重连的间隔会越来越长,1000毫秒是初始化的间隔时间,10代表尝试重连次数。
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(curatorProperties.getBaseSleepTimeMs(), curatorProperties.getMaxRetries());
        // 创建client
        CuratorFramework curatorFramework = null;
        if (StringUtils.isNoneBlank(zookeeperProperties.getUsername(), zookeeperProperties.getPassword())) {
            String auth = zookeeperProperties.getUsername() + ":" + zookeeperProperties.getPassword();
            curatorFramework = CuratorFrameworkFactory.builder()
                    .authorization(SCHEME_DIGEST, auth.getBytes())
                    .connectString(zookeeperProperties.getConnectString())
                    .retryPolicy(retry)
                    .build();
        } else {
            curatorFramework = CuratorFrameworkFactory.newClient(zookeeperProperties.getConnectString(), retry);
        }
        // 添加watched 监听器
        curatorFramework.getCuratorListenable().addListener(curatorListener);
        curatorFramework.start();
        return curatorFramework;
    }

}
