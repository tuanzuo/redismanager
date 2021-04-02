package com.tz.redismanager.limiter.config;

import com.tz.redismanager.limiter.annotation.EnableLimiterAutoConfiguration;
import com.tz.redismanager.limiter.annotation.Limiter;
import com.tz.redismanager.limiter.aspect.ILimiterAspectConfigCustomizer;
import com.tz.redismanager.limiter.aspect.LimiterAspect;
import com.tz.redismanager.limiter.domain.ResultCode;
import com.tz.redismanager.limiter.exception.LimiterException;
import com.tz.redismanager.limiter.service.ILimiterConfigService;
import com.tz.redismanager.limiter.service.ILimiterService;
import com.tz.redismanager.limiter.service.impl.LimiterConfigServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.HashMap;
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
    /**
     * @see EnableLimiterAutoConfiguration#initLimiter()
     */
    private static final String INIT_LIMITER = "initLimiter";
    /**
     * @see EnableLimiterAutoConfiguration#initLimiterScanPackage()
     */
    private static final String INIT_LIMITER_SCAN_PACKAGE = "initLimiterScanPackage";

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
    @ConditionalOnMissingBean(ILimiterConfigService.class)
    public ILimiterConfigService limiterConfigService() {
        return new LimiterConfigServiceImpl();
    }

    @Bean
    @Primary
    public ILimiterService limiterService(List<ILimiterService> services, ILimiterConfigService limiterConfigService) {
        String limiterType = limiterAutoConfiguration.getString(LIMITER_TYPE);
        ILimiterService limiterService = services.stream()
                .filter((service) -> service.support(limiterType))
                .findFirst()
                .orElseThrow(() -> new LimiterException(ResultCode.ENABLE_LIMITER_TYPE_NOT_SUPPORT.getCode(), "@EnableLimiterAutoConfiguration is not support limiterType-->" + limiterType));
        //初始化限流器
        this.initLimiter(limiterService, limiterConfigService);
        return limiterService;
    }

    @Bean
    public LimiterAspect limiterAspect(ILimiterService limiterService, ILimiterConfigService limiterConfigService, @Autowired(required = false) ILimiterAspectConfigCustomizer configCustomizer) {
        /**【2】通过反射直接修改@Order注解的value值的方式修改缓存失效切面({@link LimiterAspect})的Order*/
        /*if (null != configCustomizer) {
            int customizeOrder = configCustomizer.customizeOrder();
            Map<String, Object> memberValues = AnnotationUtils.getAnnotationAttributes(LimiterAspect.class, Order.class);
            //重新设置Order注解的值
            memberValues.put("value", customizeOrder);
        }*/
        /**【1】通过注入ILimiterAspectConfigCustomizer服务的方式修改缓存生效切面({@link LimiterAspect})的Order*/
        return new LimiterAspect(limiterService, limiterConfigService, configCustomizer);
    }

    private void initLimiter(ILimiterService limiterService, ILimiterConfigService limiterConfigService) {
        if (!limiterAutoConfiguration.getBoolean(INIT_LIMITER)) {
            logger.info("[未开启初始化限流器] [{@link EnableLimiterAutoConfiguration#initLimiter()}]");
            return;
        }
        String initScanPackage = limiterAutoConfiguration.getString(INIT_LIMITER_SCAN_PACKAGE);
        if (StringUtils.isBlank(initScanPackage)) {
            logger.info("[初始化限流器] [扫描的包路径不能为空] [{@link EnableLimiterAutoConfiguration#initLimiterScanPackage()}]");
            return;
        }
        Map<String, String> keyMap = new HashMap();
        //设置扫描路径
        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(initScanPackage)).setScanners(new MethodAnnotationsScanner()));
        //扫描包内带有@Limiter注解的所有方法集合
        Set<Method> methods = reflections.getMethodsAnnotatedWith(Limiter.class);
        //循环获取方法
        methods.forEach(method -> {
            Limiter limiter = method.getAnnotation(Limiter.class);
            if (null == keyMap.putIfAbsent(limiter.key(), limiter.key())) {
                LimiterConfig limiterConfig = limiterConfigService.convertLimiter(limiter);
                logger.info("[初始化限流器配置] [{}] [{}] [完成]", limiter.key(), limiter.name());
                limiterService.initLimiter(limiterConfig);
                logger.info("[初始化限流器] [{}] [{}] [完成]", limiter.key(), limiter.name());
            }
        });
    }

}
