package com.tz.redismanager.limiter.config;

import com.tz.redismanager.limiter.aspect.LimiterAspect;
import com.tz.redismanager.limiter.domain.Limiter;
import com.tz.redismanager.limiter.domain.ResultCode;
import com.tz.redismanager.limiter.exception.LimiterException;
import com.tz.redismanager.limiter.service.ILimiterService;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>Limiter ConfigurationSelector</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-20 23:05
 * @see org.springframework.context.annotation.MBeanExportConfiguration
 **/
public class LimiterConfigurationSelector implements ImportAware, EnvironmentAware, BeanFactoryAware {

    private static final Logger logger = LoggerFactory.getLogger(LimiterConfigurationSelector.class);

    /**
     * @see EnableLimiterAutoConfiguration#limiterType()
     */
    private static final String LIMITER_TYPE = "limiterType";
    private static final String INIT_TO_START = "initLimiterInStart";
    private static final String INIT_SCAN_PACKAGE = "initLimiterToScanPackage";

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
        //初始化限流器
        this.initLimiter(limiterService);
        return limiterService;
    }

    @Bean
    public LimiterAspect limiterAspect(ILimiterService limiterService) {
        return new LimiterAspect(limiterService);
    }

    private void initLimiter(ILimiterService limiterService) {
        boolean initToStart = limiterAutoConfiguration.getBoolean(INIT_TO_START);
        if (!initToStart) {
            return;
        }
        String initScanPackage = limiterAutoConfiguration.getString(INIT_SCAN_PACKAGE);
        if (StringUtils.isBlank(initScanPackage)) {
            return;
        }
        //设置扫描路径
        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(initScanPackage)).setScanners(new MethodAnnotationsScanner()));
        //扫描包内带有@Limiter注解的所有方法集合
        Set<Method> methods = reflections.getMethodsAnnotatedWith(Limiter.class);
        //循环获取方法
        methods.forEach(method -> {
            Limiter limiter = method.getDeclaredAnnotation(Limiter.class);
            limiterService.initLimiter(limiter);
            logger.info("[初始化限流器] [{}({})完成]",limiter.key(),limiter.name());
        });
    }

}
