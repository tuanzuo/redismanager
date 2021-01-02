package com.tz.redismanager.cacher.config;

import com.tz.redismanager.cacher.aspect.CacherAspect;
import com.tz.redismanager.cacher.domain.ResultCode;
import com.tz.redismanager.cacher.exception.CacherException;
import com.tz.redismanager.cacher.service.ICacheService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

/**
 * <p>Cacher ConfigurationSelector</p>
 *
 * @version 1.6.0
 * @time 2020-12-20 23:05
 * @see org.springframework.context.annotation.MBeanExportConfiguration
 **/
public class CacherConfigurationSelector implements ImportAware, EnvironmentAware, BeanFactoryAware {

    /**
     * @see EnableCacherAutoConfiguration#cacherType()
     */
    private static final String CACHER_TYPE = "cacherType";

    @Nullable
    private AnnotationAttributes cacherAutoConfiguration;

    @Nullable
    private Environment environment;

    @Nullable
    private BeanFactory beanFactory;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        Map<String, Object> map = importMetadata.getAnnotationAttributes(EnableCacherAutoConfiguration.class.getName());
        this.cacherAutoConfiguration = AnnotationAttributes.fromMap(map);
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
    @Primary
    public ICacheService limiterService(List<ICacheService> services) {
        String limiterType = cacherAutoConfiguration.getString(CACHER_TYPE);
        ICacheService limiterService = services.stream()
                .filter((service) -> service.support(limiterType))
                .findFirst()
                .orElseThrow(() -> new CacherException(ResultCode.ENABLE_CACHER_TYPE_NOT_SUPPORT.getCode(), "@EnableLimiterAutoConfiguration is not support limiterType-->" + limiterType));
        return limiterService;
    }

    @Bean
    public CacherAspect limiterAspect(ICacheService cacheService) {
        return new CacherAspect(cacheService);
    }

}
