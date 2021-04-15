package com.tz.redismanager.config.notify.zookeeper.curator;

import com.tz.redismanager.config.dao.IConfigDao;
import com.tz.redismanager.config.domain.param.ConfigQueryParam;
import com.tz.redismanager.config.domain.po.ConfigPO;
import com.tz.redismanager.config.service.IConfigChangeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-16 0:21
 **/
@Service
public class CustomTreeCacheListener implements TreeCacheListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private IConfigDao configDao;
    @Autowired(required = false)
    private IConfigChangeService configChangeService;

    @Override
    public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
        TreeCacheEvent.Type type = event.getType();
        if (TreeCacheEvent.Type.NODE_ADDED == type || TreeCacheEvent.Type.NODE_UPDATED == type) {
            String path = event.getData().getPath();
            logger.info("[config配置] [TreeCacheListener] {} -- {}", type, path);
            String[] pathArray = StringUtils.split(path, "/");
            if (null != configChangeService && null != pathArray && pathArray.length == 6) {
                ConfigQueryParam param = new ConfigQueryParam();
                param.setId(Integer.valueOf(pathArray[5]));
                param.setServiceName(applicationName);
                List<ConfigPO> list = configDao.selectListByParam(param);
                if (CollectionUtils.isNotEmpty(list)) {
                    ConfigPO temp = list.get(0);
                    configChangeService.change(temp);
                    logger.info("[config配置] [TreeCacheListener] [{}] [更新配置完成] [serviceName：{}] [key：{}] [type：{}] [path：{}]", type, temp.getServiceName(), temp.getKey(), temp.getType(), path);
                }
            }
        }
    }
}
