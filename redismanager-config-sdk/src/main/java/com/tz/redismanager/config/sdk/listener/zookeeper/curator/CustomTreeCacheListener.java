package com.tz.redismanager.config.sdk.listener.zookeeper.curator;

import com.tz.redismanager.config.sdk.constant.ConstInterface;
import com.tz.redismanager.config.sdk.domain.dto.ConfigContext;
import com.tz.redismanager.config.sdk.domain.dto.ConfigQueryDTO;
import com.tz.redismanager.config.sdk.service.IConfigChangeService;
import com.tz.redismanager.config.sdk.service.IFetchConfigService;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>自定义实现的TreeCache监听器</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-16 0:21
 **/
public class CustomTreeCacheListener implements TreeCacheListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.application.name}")
    private String applicationName;

    private IFetchConfigService fetchConfigService;
    private IConfigChangeService configChangeService;

    public CustomTreeCacheListener(IFetchConfigService fetchConfigService, IConfigChangeService configChangeService) {
        this.fetchConfigService = fetchConfigService;
        this.configChangeService = configChangeService;
    }

    @Override
    public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
        TreeCacheEvent.Type type = event.getType();
        if (TreeCacheEvent.Type.NODE_ADDED == type || TreeCacheEvent.Type.NODE_UPDATED == type) {
            String path = event.getData().getPath();
            logger.info("[ConfigSdk配置] [zookeeper监听器] type={} -- path={}", type, path);
            String[] pathArray = StringUtils.split(path, ConstInterface.Symbol.SLASH);
            if (null != configChangeService && null != pathArray && pathArray.length == 6) {
                List<ConfigContext> list = Optional.ofNullable(fetchConfigService.fetchConfig(this.buildConfigQueryDTO(pathArray[5]))).orElse(new ArrayList<>());
                ConfigContext temp = list.get(0);
                configChangeService.change(temp);
                logger.info("[ConfigSdk配置] [zookeeper监听器] [{}] [更新配置完成] [serviceName：{}] [key：{}] [type：{}] [path：{}]", type, temp.getServiceName(), temp.getConfigKey(), temp.getConfigType(), path);
            }
        }
    }

    private ConfigQueryDTO buildConfigQueryDTO(String id) {
        ConfigQueryDTO param = new ConfigQueryDTO();
        param.setId(Integer.valueOf(id));
        param.setServiceName(applicationName);
        return param;
    }
}
