package com.tz.redismanager.config.sdk.listener.zookeeper.curator;

import com.tz.redismanager.config.sdk.constant.ConstInterface;
import com.tz.redismanager.config.sdk.listener.zookeeper.ZookeeperSdkProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>curator配置</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-08 22:41
 **/
public class CuratorSdkConfig {

    private static final Logger logger = LoggerFactory.getLogger(CuratorSdkConfig.class);

    private static final String SCHEME_DIGEST = "digest";

    private ZookeeperSdkProperties zookeeperSdkProperties;
    private CuratorSdkProperties curatorSdkProperties;

    public CuratorSdkConfig(ZookeeperSdkProperties zookeeperSdkProperties, CuratorSdkProperties curatorSdkProperties) {
        this.zookeeperSdkProperties = zookeeperSdkProperties;
        this.curatorSdkProperties = curatorSdkProperties;
    }

    /**
     * Curator Apache
     * 提供访问Zookeeper的工具包，封装了这些低级别操作同时也提供一些高级服务，比如分布式锁、领导选取
     *
     * @return
     */
    public CuratorFramework createCuratorFramework() {
        // ExponentialBackoffRetry是种重连策略，每次重连的间隔会越来越长,1000毫秒是初始化的间隔时间,10代表尝试重连次数。
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(curatorSdkProperties.getBaseSleepTimeMs(), curatorSdkProperties.getMaxRetries());
        // 创建client
        CuratorFramework curatorFramework = null;
        if (StringUtils.isNoneBlank(zookeeperSdkProperties.getUsername(), zookeeperSdkProperties.getPassword())) {
            String auth = StringUtils.join(zookeeperSdkProperties.getUsername(), ConstInterface.Symbol.COLON, zookeeperSdkProperties.getPassword());
            curatorFramework = CuratorFrameworkFactory.builder()
                    .authorization(SCHEME_DIGEST, auth.getBytes())
                    .connectString(zookeeperSdkProperties.getConnectString())
                    .retryPolicy(retry)
                    .build();
        } else {
            curatorFramework = CuratorFrameworkFactory.newClient(zookeeperSdkProperties.getConnectString(), retry);
        }
        curatorFramework.start();
        return curatorFramework;
    }

    public static void addWatcherWithTreeCache(CuratorFramework curatorFramework, TreeCacheListener treeCacheListener, String path) throws Exception {
        CuratorCache curatorCache = CuratorCache.builder(curatorFramework, path).build();
        CuratorCacheListener listener = CuratorCacheListener.builder()
                .forTreeCache(curatorFramework, treeCacheListener)
                .build();
        curatorCache.listenable().addListener(listener);
        curatorCache.start();
        logger.info("[ConfigSdk配置] [监听器启动完成] path={}", path);
    }

}
