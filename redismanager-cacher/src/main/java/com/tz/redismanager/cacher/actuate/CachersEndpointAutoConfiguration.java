package com.tz.redismanager.cacher.actuate;

import com.tz.redismanager.cacher.service.ICacheService;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * <p>缓存器Endpoint的自动配置</p>
 * @see org.springframework.boot.actuate.autoconfigure.logging.LoggersEndpointAutoConfiguration
 * @author tuanzuo
 * @version 1.6.0
 * @time 2021-01-21 0:41
 **/
@Component
@Configuration(proxyBeanMethods = false)
@ConditionalOnAvailableEndpoint(endpoint = CachersEndpoint.class)
public class CachersEndpointAutoConfiguration {

    @Bean
	@ConditionalOnBean(ICacheService.class)
    @ConditionalOnMissingBean
    public CachersEndpoint cacherEndpoint(ICacheService cacheService) {
        return new CachersEndpoint(cacheService);
    }


}