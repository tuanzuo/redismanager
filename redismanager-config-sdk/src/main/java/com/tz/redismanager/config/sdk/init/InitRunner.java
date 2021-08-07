package com.tz.redismanager.config.sdk.init;

import com.tz.redismanager.config.sdk.domain.dto.ConfigContext;
import com.tz.redismanager.config.sdk.domain.dto.ConfigQueryDTO;
import com.tz.redismanager.config.sdk.service.IConfigChangeService;
import com.tz.redismanager.config.sdk.service.IFetchConfigService;
import org.apache.commons.lang3.StringUtils;
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
 * <p>初始化的Runner</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-08-07 19:16
 **/
@Component
public class InitRunner implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired(required = false)
    private IConfigChangeService configChangeService;
    @Autowired
    private IFetchConfigService fetchConfigService;

    @Override
    public void run(String... args) throws Exception {
        if (StringUtils.isBlank(applicationName)) {
            logger.error("[ConfigSdk配置] applicationName不能为空");
            throw new RuntimeException("applicationName不能为空");
        }
        if (null != configChangeService) {
            //应用启动拉取配置数据，进行更新
            List<ConfigContext> configs = Optional.ofNullable(fetchConfigService.fetchConfig(this.buildConfigQuery())).orElse(new ArrayList<>());
            for (ConfigContext temp : configs) {
                configChangeService.change(temp);
                logger.info("[ConfigSdk配置] [ConfigRunner] [应用启动] [更新配置完成] [serviceName：{}] [key：{}] [type：{}]", temp.getServiceName(), temp.getConfigKey(), temp.getConfigType());
            }
        }
    }

    private ConfigQueryDTO buildConfigQuery() {
        ConfigQueryDTO param = new ConfigQueryDTO();
        param.setServiceName(applicationName);
        return param;
    }
}
