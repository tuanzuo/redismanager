package com.tz.redismanager.config.sdk.listener.zookeeper.curator;

import com.tz.redismanager.config.sdk.listener.zookeeper.ZookeeperSdkProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

/**
 * <p>curator的Runner</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-09 21:17
 **/
public class CuratorRunner implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.application.name}")
    private String applicationName;

    private ZookeeperSdkProperties zookeeperSdkProperties;
    private CuratorFrameworkCompose curatorFrameworkCompose;
    private CustomTreeCacheListener treeCacheListener;

    public CuratorRunner(ZookeeperSdkProperties zookeeperSdkProperties, CuratorFrameworkCompose curatorFrameworkCompose, CustomTreeCacheListener treeCacheListener) {
        this.zookeeperSdkProperties = zookeeperSdkProperties;
        this.curatorFrameworkCompose = curatorFrameworkCompose;
        this.treeCacheListener = treeCacheListener;
    }

    @Override
    public void run(String... args) throws Exception {
        if (StringUtils.isBlank(applicationName)) {
            logger.error("[ConfigSdk配置] applicationName不能为空");
            throw new RuntimeException("applicationName不能为空");
        }
        CuratorFramework curatorFramework = curatorFrameworkCompose.getCuratorFramework();
        String appNamePath = zookeeperSdkProperties.getParentPath() + applicationName;
        if (null != curatorFramework.checkExists().forPath(appNamePath)) {
            curatorFramework.delete().deletingChildrenIfNeeded().forPath(appNamePath);
        }
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(appNamePath);
        CuratorSdkConfig.addWatcherWithTreeCache(curatorFramework, appNamePath, treeCacheListener);
        logger.info("[ConfigSdk配置] [添加path的监听] {}", appNamePath);

    }
}
