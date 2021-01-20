package com.tz.redismanager.cacher.actuate;

import com.github.benmanes.caffeine.cache.LoadingCache;
import com.tz.redismanager.cacher.service.ICacheService;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
        Map<String, LoadingCache<String, String>> l1Cachers = cacheService.getL1Cachers();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("l1CacherCount", l1Cachers.size());
        List<CacherData> details = new ArrayList<>();
        l1Cachers.forEach((key, value) -> {
            CacherData data = new CacherData();
            data.setName(key);
            data.setEstimatedSize(l1Cachers.get(key).estimatedSize());
            details.add(data);
        });
        result.put("details", details);
        return result;
    }

    /**
     * 查询指定name的缓存器
     * @see <a href="http://127.0.0.1/actuator/cachers/{name}">查询URL</a>
     */
    @ReadOperation
    public Map<String, Object> cacher(@Selector String name) {
        Map<String, LoadingCache<String, String>> l1Cachers = cacheService.getL1Cachers();
        LoadingCache<String, String> l1Cache = l1Cachers.get(name);
        Map<String, Object> result = new LinkedHashMap<>();
        if (null == l1Cache) {
            return result;
        }
        result.put("name", name);
        result.put("estimatedSize", l1Cache.estimatedSize());
        result.put("cacheStats", this.buildCacheStats(l1Cache));
        return result;
    }

    private CacheStats buildCacheStats(LoadingCache<String, String> l1Cache) {
        com.github.benmanes.caffeine.cache.stats.@NonNull CacheStats stats = l1Cache.stats();
        CacheStats cacheStats = new CacheStats();
        cacheStats.setHitCount(stats.hitCount());
        cacheStats.setMissCount(stats.missCount());
        cacheStats.setHitRate(stats.hitRate());
        cacheStats.setMissRate(stats.missRate());
        cacheStats.setLoadSuccessCount(stats.loadSuccessCount());
        cacheStats.setLoadFailureCount(stats.loadFailureCount());
        cacheStats.setTotalLoadTime(stats.totalLoadTime());
        cacheStats.setEvictionCount(stats.evictionCount());
        cacheStats.setEvictionWeight(stats.evictionWeight());
        stats.missRate();
        stats.hitRate();
        return cacheStats;
    }

    @Getter
    @Setter
    public static class CacherData {
        private String name;
        private Long estimatedSize;
    }

    @Getter
    @Setter
    public static class CacheStats {
        private long hitCount;
        private long missCount;
        private double missRate;
        private double hitRate;
        private long loadSuccessCount;
        private long loadFailureCount;
        private long totalLoadTime;
        private long evictionCount;
        private long evictionWeight;
    }

}
