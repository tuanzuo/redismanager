package com.tz.redismanager.cacher.actuate;

import com.tz.redismanager.cacher.service.ICacheService;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;

import java.util.Map;

/**
 * <p>缓存器Endpoint</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2021-01-21 0:41
 * @see org.springframework.boot.actuate.logging.LoggersEndpoint
 **/
@Endpoint(id = "cachers")
public class CachersEndpoint {

    private ICacheService cacheService;

    public CachersEndpoint(ICacheService cacheService) {
        this.cacheService = cacheService;
    }

    /**
     * 查询所有缓存器
     * @see <a href="http://127.0.0.1/actuator/cachers">查询URL</a>
     */
    @ReadOperation
    public Map<String, Object> cachers() {
        return cacheService.getL1CacherInfo(null);
    }

    /**
     * 查询指定name的缓存器
     * @see <a href="http://127.0.0.1/actuator/cachers/{name}">查询URL</a>
     */
    @ReadOperation
    public Map<String, Object> cacher(@Selector String name) {
        return cacheService.getL1CacherInfo(name);
    }

}
