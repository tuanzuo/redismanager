package com.tz.redismanager.config.sdk.config;

import com.tz.redismanager.config.sdk.annotation.EnableConfigSdkAutoConfiguration;
import com.tz.redismanager.config.sdk.listener.zookeeper.ZookeeperSdkProperties;
import com.tz.redismanager.config.sdk.listener.zookeeper.curator.*;
import com.tz.redismanager.config.sdk.service.IConfigChangeService;
import com.tz.redismanager.config.sdk.service.IFetchConfigService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * <p>配置sdk ConfigurationSelector</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-25 22:21
 * @see org.springframework.context.annotation.MBeanExportConfiguration
 **/
public class ConfigSdkConfigurationSelector implements ImportAware, EnvironmentAware, BeanFactoryAware {

    private static final Logger logger = LoggerFactory.getLogger(ConfigSdkConfigurationSelector.class);

    @Nullable
    private AnnotationAttributes configAutoConfiguration;

    @Nullable
    private Environment environment;

    @Nullable
    private BeanFactory beanFactory;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        Map<String, Object> map = importMetadata.getAnnotationAttributes(EnableConfigSdkAutoConfiguration.class.getName());
        this.configAutoConfiguration = AnnotationAttributes.fromMap(map);
        //configAutoConfiguration.getString(CONFIG_SYNC_TYPE);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Configuration
    @ConditionalOnClass({ZooKeeper.class})
    public static class ZookeeperConfig {

        @Bean
        @ConditionalOnExpression("#{(configSdkProperties.ZOOKEEPER).equals(configSdkProperties.configSyncType)}")
        public ZookeeperSdkProperties zookeeperSdkProperties() {
            return new ZookeeperSdkProperties();
        }

        @Configuration
        @ConditionalOnClass({CuratorFramework.class})
        public static class CuratorConfig {

            @Bean
            @ConditionalOnExpression("#{(configSdkProperties.ZOOKEEPER_CURATOR).equals(configSdkProperties.configSyncSubType)}")
            public CuratorSdkProperties curatorSdkProperties() {
                return new CuratorSdkProperties();
            }

            @Bean
            @ConditionalOnExpression("#{(configSdkProperties.ZOOKEEPER_CURATOR).equals(configSdkProperties.configSyncSubType)}")
            public CustomTreeCacheListener customTreeCacheListener(IFetchConfigService fetchConfigService, @Autowired(required = false) IConfigChangeService configChangeService) {
                return new CustomTreeCacheListener(fetchConfigService, configChangeService);
            }

            @Bean
            @ConditionalOnExpression("#{(configSdkProperties.ZOOKEEPER_CURATOR).equals(configSdkProperties.configSyncSubType)}")
            public CuratorFrameworkCompose curatorSdkConfig(ZookeeperSdkProperties zookeeperProperties, CuratorSdkProperties curatorSdkProperties, @Autowired(required = false) CuratorFramework curatorFramework) {
                return new CuratorSdkConfig(zookeeperProperties, curatorSdkProperties).curatorFrameworkCompose(curatorFramework);
            }

            @Bean
            @ConditionalOnExpression("#{(configSdkProperties.ZOOKEEPER_CURATOR).equals(configSdkProperties.configSyncSubType)}")
            public CuratorRunner curatorRunner(ZookeeperSdkProperties zookeeperSdkProperties, CuratorFrameworkCompose curatorFrameworkCompose, CustomTreeCacheListener treeCacheListener) {
                return new CuratorRunner(zookeeperSdkProperties, curatorFrameworkCompose, treeCacheListener);
            }

        }
    }

}
