package com.tz.redismanager.config.runner;

import com.tz.redismanager.config.constant.ConstInterface;
import com.tz.redismanager.config.dao.IConfigDao;
import com.tz.redismanager.config.domain.param.ConfigQueryParam;
import com.tz.redismanager.config.domain.po.ConfigPO;
import com.tz.redismanager.config.enm.ConfigTypeEnum;
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
import com.tz.redismanager.config.notice.zookeeper.curator.CustomCuratorListener;

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
public class ConfigRunner implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String parentPath = ConstInterface.Zookeeper.BASE_PATH;

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private CuratorFramework curatorFramework;

    @Autowired
    private IConfigDao configDao;

    @Override
    public void run(String... args) throws Exception {
        if (StringUtils.isBlank(applicationName)) {
            logger.error("[config配置] applicationName不能为空");
            throw new RuntimeException("applicationName不能为空");
        }

        String appNamePath = parentPath + applicationName;
        Stat stat = curatorFramework.checkExists().forPath(appNamePath);
        if (null == stat) {
            appNamePath = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(appNamePath);
            logger.info("[config配置] [创建path] {}", appNamePath);
        }

        for (ConfigTypeEnum value : ConfigTypeEnum.values()) {
            String configTypePath = appNamePath + "/" + value.getName();
            stat = curatorFramework.checkExists().forPath(configTypePath);
            if (null == stat) {
                configTypePath = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(configTypePath);
                logger.info("[config配置] [创建path] {}", configTypePath);
            }
            /*curatorFramework.watchers().add().forPath(configTypePath);
            logger.info("[config配置] [添加path的监听] {}", configTypePath);*/
        }

        ConfigQueryParam param = new ConfigQueryParam();
        param.setServiceName(applicationName);
        List<ConfigPO> configs = Optional.ofNullable(configDao.selectListByParam(param)).orElse(new ArrayList<>());
        for (ConfigPO temp : configs) {
            String keyPath = appNamePath + "/" + ConfigTypeEnum.getByCode(temp.getType()).getName() + "/" + temp.getKey() + "/" + temp.getId();
            stat = curatorFramework.checkExists().forPath(keyPath);
            if (null == stat) {
                keyPath = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(keyPath, String.valueOf(temp.getId()).getBytes());
                logger.info("[config配置] [创建path] {}", keyPath);
            }
            curatorFramework.getData().watched().forPath(keyPath);
            logger.info("[config配置] [添加path的监听] {}", keyPath);

            /**设置节点数据的目的是为了触发“NodeDataChanged”事件的监听{@link CustomCuratorListener}-->这样应用启动的时候就可以更新最新的配置*/
            curatorFramework.setData().forPath(keyPath, String.valueOf(temp.getId()).getBytes());
            logger.info("[config配置] [给path节点设置data] {}", keyPath);
        }
    }
}
