package com.tz.redismanager.security;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.exception.RmException;
import com.tz.redismanager.service.IAuthCacheService;
import com.tz.redismanager.service.ICipherService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * <p>认证ConfigurationSelector</p>
 *
 * @version 1.5.0
 * @time 2020-11-05 23:55
 * @see org.springframework.context.annotation.MBeanExportConfiguration
 **/
@Configuration
public class AuthConfigurationSelector implements ImportAware, EnvironmentAware, BeanFactoryAware {

    /**
     * @see EnableAuth#authType()
     */
    private static final String ENABLEAUTH_AUTHTYPE = "authType";

    @Nullable
    private AnnotationAttributes enableAuth;

    @Nullable
    private Environment environment;

    @Nullable
    private BeanFactory beanFactory;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        Map<String, Object> map = importMetadata.getAnnotationAttributes(EnableAuth.class.getName());
        this.enableAuth = AnnotationAttributes.fromMap(map);
        if (this.enableAuth == null) {
            throw new RmException(ResultCode.ENABLE_AUTH_NOT_PRESENT.getCode(), "@EnableAuth is not present on importing class " + importMetadata.getClassName());
        }
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
    public ITokenAuthService authService(IAuthCacheService authCacheService, ICipherService cipherService, StringRedisTemplate stringRedisTemplate) {
        String authType = enableAuth.getString(ENABLEAUTH_AUTHTYPE);
        if (ConstInterface.AuthType.REDIS.equals(authType)) {
            return new RedisTokenAuthServiceImpl(authCacheService, cipherService, stringRedisTemplate);
        }
        if (ConstInterface.AuthType.JWT.equals(authType)) {
            return new JWTTokenAuthServiceImpl();
        }
        throw new RmException(ResultCode.ENABLE_AUTH_TYPE_NOT_SUPPORT.getCode(), "@EnableAuth is not support authType-->" + authType);
    }

}
