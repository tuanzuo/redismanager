package com.tz.redismanager.config.config;

import com.tz.redismanager.config.annotation.EnableConfigAutoConfiguration;
import com.tz.redismanager.config.dao.IConfigDao;
import com.tz.redismanager.config.dao.impl.JdbcTemplateConfigDaoImpl;
import com.tz.redismanager.config.notify.INotiyService;
import com.tz.redismanager.config.notify.zookeeper.ZookeeperProperties;
import com.tz.redismanager.config.notify.zookeeper.curator.CuratorConfig;
import com.tz.redismanager.config.notify.zookeeper.curator.CuratorNotifyServiceImpl;
import com.tz.redismanager.config.notify.zookeeper.curator.CuratorProperties;
import com.tz.redismanager.config.service.IConfigService;
import com.tz.redismanager.config.service.impl.ConfigServiceImpl;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

/**
 * <p>配置 ConfigurationSelector</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-25 22:21
 * @see org.springframework.context.annotation.MBeanExportConfiguration
 **/
public class ConfigConfigurationSelector implements ImportAware, EnvironmentAware, BeanFactoryAware {

    private static final Logger logger = LoggerFactory.getLogger(ConfigConfigurationSelector.class);

    @Nullable
    private AnnotationAttributes configAutoConfiguration;

    @Nullable
    private Environment environment;

    @Nullable
    private BeanFactory beanFactory;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        Map<String, Object> map = importMetadata.getAnnotationAttributes(EnableConfigAutoConfiguration.class.getName());
        this.configAutoConfiguration = AnnotationAttributes.fromMap(map);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Bean
    @ConditionalOnMissingBean(IConfigDao.class)
    public IConfigDao configDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate, ConfigProperties configProperties) {
        return new JdbcTemplateConfigDaoImpl(namedParameterJdbcTemplate, configProperties);
    }

    @Bean
    @ConditionalOnMissingBean(IConfigService.class)
    public IConfigService configService(IConfigDao configDao, ApplicationContext applicationContext) {
        return new ConfigServiceImpl(configDao, applicationContext);
    }

    @Bean
    @Primary
    public INotiyService notiyService(List<INotiyService> services, ConfigProperties configProperties) {
        return services.stream().filter(temp -> temp.support(configProperties.getConfigSyncSubType())).findFirst().get();
    }


    @Configuration
    @ConditionalOnClass({ZooKeeper.class})
    public static class ZookeeperConfig {

        @Bean
        @ConditionalOnExpression("#{(configProperties.ZOOKEEPER).equals(configProperties.configSyncType)}")
        public ZookeeperProperties zookeeperProperties() {
            return new ZookeeperProperties();
        }

        @Configuration
        @ConditionalOnClass({CuratorFramework.class})
        public static class CuratorConfiguration {

            @Bean
            @ConditionalOnExpression("#{(configProperties.ZOOKEEPER_CURATOR).equals(configProperties.configSyncSubType)}")
            public CuratorProperties curatorProperties() {
                return new CuratorProperties();
            }

            @Bean
            @ConditionalOnMissingBean(CuratorFramework.class)
            @ConditionalOnExpression("#{(configProperties.ZOOKEEPER_CURATOR).equals(configProperties.configSyncSubType)}")
            public CuratorFramework curatorFramework(ZookeeperProperties zookeeperProperties, CuratorProperties curatorProperties) {
                return new CuratorConfig(zookeeperProperties, curatorProperties).createCuratorFramework();
            }

            @Bean
            @ConditionalOnExpression("#{(configProperties.ZOOKEEPER_CURATOR).equals(configProperties.configSyncSubType)}")
            public INotiyService curatorNotifyServiceImpl(ZookeeperProperties zookeeperProperties, CuratorFramework curatorFramework) {
                return new CuratorNotifyServiceImpl(zookeeperProperties, curatorFramework);
            }

        }
    }


}
