package com.tz.redismanager.config.notice.zookeeper.curator;

import com.tz.redismanager.config.constant.ConstInterface;
import com.tz.redismanager.config.dao.IConfigDao;
import com.tz.redismanager.config.domain.po.ConfigPO;
import com.tz.redismanager.config.service.IConfigChangeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-08 22:42
 **/
@Service
public class CustomCuratorListener implements CuratorListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IConfigDao configDao;
    @Autowired(required = false)
    private IConfigChangeService configChangeService;


    @Override
    public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
        CuratorEventType type = event.getType();
        if (type == CuratorEventType.WATCHED) {
            WatchedEvent watchedEvent = event.getWatchedEvent();
            Watcher.Event.EventType eventType = watchedEvent.getType();
            if (eventType == Watcher.Event.EventType.NodeCreated || eventType == Watcher.Event.EventType.NodeDeleted || eventType == Watcher.Event.EventType.NodeDataChanged) {
                String path = watchedEvent.getPath();
                if(path.startsWith(ConstInterface.Zookeeper.BASE_PATH)){
                    String[] pathArray = StringUtils.split(path, "/");
                    if (null != configChangeService && null != pathArray && pathArray.length == 6) {
                        ConfigPO temp = configDao.selectByPrimaryKey(Integer.valueOf(pathArray[5]));
                        configChangeService.change(temp);
                        logger.info("[config配置] [更新配置完成] [serviceName：{} key：{} type：{}]", temp.getServiceName(), temp.getKey(), temp.getType());
                    }
                }

                logger.info("[config配置] [CuratorListener] " + watchedEvent.getType() + " -- " + path + " -- ");
                // 重新设置该节点监听
                if (null != path) {
                    client.checkExists().watched().forPath(path);
                }
            }
        }
    }

}
