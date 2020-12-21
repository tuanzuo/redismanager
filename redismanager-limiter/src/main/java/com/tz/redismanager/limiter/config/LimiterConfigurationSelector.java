package com.tz.redismanager.limiter.config;

import com.tz.redismanager.limiter.aspect.LimiterAspect;
import com.tz.redismanager.limiter.domain.ResultCode;
import com.tz.redismanager.limiter.exception.LimiterException;
import com.tz.redismanager.limiter.service.ILimiterService;
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
 * <p>Limiter ConfigurationSelector</p>
 *
 * @version 1.6.0
 * @time 2020-12-20 23:05
 * @see org.springframework.context.annotation.MBeanExportConfiguration
 **/
public class LimiterConfigurationSelector implements ImportAware, EnvironmentAware, BeanFactoryAware {

    /**
     * @see EnableLimiterAutoConfiguration#limiterType()
     */
    private static final String LIMITER_TYPE = "limiterType";

    @Nullable
    private AnnotationAttributes limiterAutoConfiguration;

    @Nullable
    private Environment environment;

    @Nullable
    private BeanFactory beanFactory;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        Map<String, Object> map = importMetadata.getAnnotationAttributes(EnableLimiterAutoConfiguration.class.getName());
        this.limiterAutoConfiguration = AnnotationAttributes.fromMap(map);
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
    public ILimiterService limiterService(List<ILimiterService> services) {
        String limiterType = limiterAutoConfiguration.getString(LIMITER_TYPE);
        ILimiterService limiterService = services.stream()
                .filter((service) -> service.support(limiterType))
                .findFirst()
                .orElseThrow(() -> new LimiterException(ResultCode.ENABLE_LIMITER_TYPE_NOT_SUPPORT.getCode(), "@EnableLimiterAutoConfiguration is not support limiterType-->" + limiterType));
        return limiterService;
    }

    @Bean
    public LimiterAspect limiterAspect(ILimiterService limiterService) {
        return new LimiterAspect(limiterService);
    }

}
