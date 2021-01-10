package com.tz.redismanager.cacher.config;

import com.tz.redismanager.cacher.annotation.Cacher;
import com.tz.redismanager.cacher.annotation.EnableCacherAutoConfiguration;
import com.tz.redismanager.cacher.aspect.CacherAspect;
import com.tz.redismanager.cacher.aspect.CacherEvictAspect;
import com.tz.redismanager.cacher.domain.ResultCode;
import com.tz.redismanager.cacher.exception.CacherException;
import com.tz.redismanager.cacher.service.ICacheService;
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
import java.util.*;

/**
 * <p>Cacher ConfigurationSelector</p>
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
    @Primary
    public ICacheService cacheService(List<ICacheService> services) {
        String cacherType = cacherAutoConfiguration.getString(CACHER_TYPE);
        ICacheService cacheService = services.stream()
                .filter((service) -> service.support(cacherType))
                .findFirst()
                .orElseThrow(() -> new CacherException(ResultCode.ENABLE_CACHER_TYPE_NOT_SUPPORT.getCode(), "@EnableCacherAutoConfiguration is not support cacherType-->" + cacherType));
        //初始化缓存器
        this.initCacher(cacheService);
        return cacheService;
    }

    @Bean
    public CacherAspect cacherAspect(ICacheService cacheService) {
        return new CacherAspect(cacheService);
    }

    @Bean
    public CacherEvictAspect cacherEvictAspect(ICacheService cacheService) {
        return new CacherEvictAspect(cacheService);
    }

    private void initCacher(ICacheService cacheService) {
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
        Set<Method> methods = reflections.getMethodsAnnotatedWith(Cacher.class);
        //循环获取方法
        methods.forEach(method -> {
            Cacher cacher = method.getAnnotation(Cacher.class);
            if (null == keyMap.putIfAbsent(cacher.key(), cacher.key())) {
                cacheService.initCacher(cacher);
                logger.info("[初始化缓存器] [{}] [{}] [完成]", cacher.key(), cacher.name());
            }
        });
    }
}
