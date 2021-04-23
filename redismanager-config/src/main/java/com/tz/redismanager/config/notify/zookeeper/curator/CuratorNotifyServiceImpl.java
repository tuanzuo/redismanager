package com.tz.redismanager.config.notify.zookeeper.curator;

import com.tz.redismanager.config.domain.po.ConfigPO;
import com.tz.redismanager.config.enm.ConfigTypeEnum;
import com.tz.redismanager.config.notify.INotiyService;
import com.tz.redismanager.config.notify.zookeeper.ZookeeperProperties;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-15 23:31
 **/
@Service
public class CuratorNotifyServiceImpl implements INotiyService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ZookeeperProperties zookeeperProperties;
    @Autowired
    private CuratorFramework curatorFramework;

    @Override
    public void add(ConfigPO po) {
        this.notify(po);
    }

    @Override
    public void update(ConfigPO po) {
        this.notify(po);
    }

    private void notify(ConfigPO po) {
        try {
            String appNamePath = zookeeperProperties.getParentPath() + po.getServiceName();
            String keyPath = appNamePath + "/" + ConfigTypeEnum.getByCode(po.getType()).getName() + "/" + po.getKey() + "/" + po.getId();
            Stat stat = curatorFramework.checkExists().forPath(keyPath);
            if (null == stat) {
                keyPath = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(keyPath, String.valueOf(po.getId()).getBytes());
                logger.info("[config配置] [创建path] {}", keyPath);
            } else {
                curatorFramework.setData().forPath(keyPath, String.valueOf(po.getId()).getBytes());
                logger.info("[config配置] [更新path] {}", keyPath);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}