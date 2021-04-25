package com.tz.redismanager.config.config;

import com.tz.redismanager.config.annotation.EnableConfigAutoConfiguration;
import com.tz.redismanager.config.dao.IConfigDao;
import com.tz.redismanager.config.dao.impl.JdbcTemplateConfigDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-25 22:21
 * @see org.springframework.context.annotation.MBeanExportConfiguration
 **/
public class ConfigConfigurationSelector implements ImportAware, EnvironmentAware, BeanFactoryAware {

    private static final Logger logger = LoggerFactory.getLogger(ConfigConfigurationSelector.class);

    /**
     * @see EnableConfigAutoConfiguration#configSyncType()
     */
    private static final String CONFIG_SYNC_TYPE = "configSyncType";

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

}
