package com.tz.redismanager.config.notify.zookeeper.curator;

import com.tz.redismanager.config.dao.IConfigDao;
import com.tz.redismanager.config.domain.param.ConfigQueryParam;
import com.tz.redismanager.config.domain.po.ConfigPO;
import com.tz.redismanager.config.notify.zookeeper.ZookeeperProperties;
import com.tz.redismanager.config.service.IConfigChangeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-09 21:17
 **/
@Component
public class CuratorRunner implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private ZookeeperProperties zookeeperProperties;
    @Autowired
    private IConfigDao configDao;
    @Autowired(required = false)
    private IConfigChangeService configChangeService;
    @Autowired
    private CuratorFramework curatorFramework;
    @Autowired
    private CustomTreeCacheListener treeCacheListener;

    @Override
    public void run(String... args) throws Exception {
        if (StringUtils.isBlank(applicationName)) {
            logger.error("[config配置] applicationName不能为空");
            throw new RuntimeException("applicationName不能为空");
        }

        Stat stat = null;
        String appNamePath = zookeeperProperties.getParentPath() + applicationName;

        ConfigQueryParam param = new ConfigQueryParam();
        param.setServiceName(applicationName);
        List<ConfigPO> configs = Optional.ofNullable(configDao.selectListByParam(param)).orElse(new ArrayList<>());
        for (ConfigPO temp : configs) {
            if (null != configChangeService) {
                configChangeService.change(temp);
                logger.info("[config配置] [ConfigRunner] [应用启动] [更新配置完成] [serviceName：{}] [key：{}] [type：{}]", temp.getServiceName(), temp.getConfigKey(), temp.getConfigType());
            }

            /*String keyPath = appNamePath + "/" + ConfigTypeEnum.getByCode(temp.getType()).getName() + "/" + temp.getKey() + "/" + temp.getId();
            stat = curatorFramework.checkExists().forPath(keyPath);
            if (null == stat) {
                keyPath = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(keyPath, String.valueOf(temp.getId()).getBytes());
                logger.info("[config配置] [创建path] {}", keyPath);
            }*/
        }

        if (null != curatorFramework.checkExists().forPath(appNamePath)) {
            curatorFramework.delete().deletingChildrenIfNeeded().forPath(appNamePath);
        }
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(appNamePath);

        /*curatorFramework.getData().watched().forPath(appNamePath);
            logger.info("[config配置] [添加path的监听] {}", appNamePath);*/
        CuratorConfig.addWatcherWithTreeCache(curatorFramework, appNamePath, treeCacheListener);
        logger.info("[config配置] [添加path的监听] {}", appNamePath);

    }
}
