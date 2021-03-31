package com.tz.redismanager.cacher.config;

import com.tz.redismanager.cacher.annotation.Cacheable;
import com.tz.redismanager.cacher.annotation.EnableCacherAutoConfiguration;
import com.tz.redismanager.cacher.aspect.CacheEvictAspect;
import com.tz.redismanager.cacher.aspect.CacheableAspect;
import com.tz.redismanager.cacher.aspect.ICacheEvictAspectConfigCustomizer;
import com.tz.redismanager.cacher.aspect.ICacheableAspectConfigCustomizer;
import com.tz.redismanager.cacher.domain.ResultCode;
import com.tz.redismanager.cacher.exception.CacherException;
import com.tz.redismanager.cacher.service.ICacheService;
import com.tz.redismanager.cacher.service.ICacherConfigService;
import com.tz.redismanager.cacher.service.impl.CacherConfigServiceImpl;
import com.tz.redismanager.cacher.util.AnnotationUtils;
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
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>Cache ConfigurationSelector</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-20 23:05
 * @see org.springframework.context.annotation.MBeanExportConfiguration
 **/
public class CacherConfigurationSelector implements ImportAware, EnvironmentAware, BeanFactoryAware {

    private static final Logger logger = LoggerFactory.getLogger(CacherConfigurationSelector.class);

    /**
     * @see EnableCacherAutoConfiguration#cacherType()
     */
    private static final String CACHER_TYPE = "cacherType";
    /**
     * @see EnableCacherAutoConfiguration#initCacher()
     */
    private static final String INIT_CACHER = "initCacher";
    /**
     * @see EnableCacherAutoConfiguration#initCacherScanPackage()
     */
    private static final String INIT_CACHER_SCAN_PACKAGE = "initCacherScanPackage";

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
    @ConditionalOnMissingBean(ICacherConfigService.class)
    public ICacherConfigService cacherConfigService() {
        return new CacherConfigServiceImpl();
    }

    @Bean
    @Primary
    public ICacheService cacheService(List<ICacheService> services, ICacherConfigService cacherConfigService) {
        String cacherType = cacherAutoConfiguration.getString(CACHER_TYPE);
        ICacheService cacheService = services.stream()
                .filter((service) -> service.support(cacherType))
                .findFirst()
                .orElseThrow(() -> new CacherException(ResultCode.ENABLE_CACHER_TYPE_NOT_SUPPORT.getCode(), "@EnableCacherAutoConfiguration is not support cacherType-->" + cacherType));
        //初始化缓存器
        this.initCacher(cacheService, cacherConfigService);
        return cacheService;
    }

    @Bean
    public CacheableAspect cacheableAspect(ICacheService cacheService, ICacherConfigService cacherConfigService, @Autowired(required = false) ICacheableAspectConfigCustomizer configCustomizer) {
        /**【1】通过注入ICacheableAspectConfigCustomizer服务的方式修改缓存生效切面({@link CacheableAspect})的Order*/
        return new CacheableAspect(cacheService, cacherConfigService, configCustomizer);
    }

    @Bean
    public CacheEvictAspect cacheEvictAspect(ICacheService cacheService, ICacherConfigService cacherConfigService, @Autowired(required = false) ICacheEvictAspectConfigCustomizer configCustomizer) {
        /**【2】通过反射直接修改@Order注解的value值的方式修改缓存失效切面({@link CacheEvictAspect})的Order*/
        if (null != configCustomizer) {
            int customizeOrder = configCustomizer.customizeOrder();
            Map<String, Object> memberValues = AnnotationUtils.getAnnotationAttributes(CacheEvictAspect.class, Order.class);
            //重新设置Order注解的值
            memberValues.put("value", customizeOrder);
        }
        return new CacheEvictAspect(cacheService, cacherConfigService);
    }

    private void initCacher(ICacheService cacheService, ICacherConfigService cacherConfigService) {
        if (!cacherAutoConfiguration.getBoolean(INIT_CACHER)) {
            logger.info("[未开启初始化缓存器] [{@link @EnableCacherAutoConfiguration#initCacher()}]");
            return;
        }
        String initScanPackage = cacherAutoConfiguration.getString(INIT_CACHER_SCAN_PACKAGE);
        if (StringUtils.isBlank(initScanPackage)) {
            logger.info("[初始化缓存器] [扫描的包路径不能为空] [{@link EnableCacherAutoConfiguration#initCacherScanPackage()}]");
            return;
        }
        Map<String, String> keyMap = new HashMap();
        //设置扫描路径
        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(initScanPackage)).setScanners(new MethodAnnotationsScanner()));
        //扫描包内带有@Cacher注解的所有方法集合
        Set<Method> methods = reflections.getMethodsAnnotatedWith(Cacheable.class);
        //循环获取方法
        methods.forEach(method -> {
            Cacheable cacheable = method.getAnnotation(Cacheable.class);
            if (null == keyMap.putIfAbsent(cacheable.key(), cacheable.key())) {
                CacheableConfig cacherConfig = cacherConfigService.convertCacheable(cacheable);
                logger.info("[初始化缓存器配置] [{}] [{}] [完成]", cacheable.key(), cacheable.name());
                cacheService.initCacher(cacherConfig);
                logger.info("[初始化缓存器] [{}] [{}] [完成]", cacheable.key(), cacheable.name());
            }
        });
    }

}
