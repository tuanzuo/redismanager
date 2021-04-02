package com.tz.redismanager.cacher.config;

import com.tz.redismanager.cacher.domain.InvalidateType;
import com.tz.redismanager.cacher.domain.InvocationStrategy;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>缓存失效配置</p>
 *
 * @author tuanzuo
 * @version 1.6.1
 * @time 2021-04-01 0:27
 **/
@Getter
@Setter
public class CacheEvictConfig {

    /**
     * 缓存名称
     */
    private String name;

    /**
     * 缓存器key
     */
    private String key;

    /**
     * 缓存失效类型，默认失效所有
     */
    private InvalidateType invalidate = InvalidateType.ALL;

    /**
     * 在方法执行前或者执行后清理缓存，默认方法执行后清理缓存
     */
    private InvocationStrategy invocation = InvocationStrategy.AFTER;

    private CacheEvictConfig(){

    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private String key;
        private InvalidateType invalidate = InvalidateType.ALL;
        private InvocationStrategy invocation = InvocationStrategy.AFTER;

        private Builder() {
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setKey(String key) {
            this.key = key;
            return this;
        }

        public Builder setInvalidate(InvalidateType invalidate) {
            this.invalidate = invalidate;
            return this;
        }

        public Builder setInvocation(InvocationStrategy invocation) {
            this.invocation = invocation;
            return this;
        }

        public CacheEvictConfig build() {
            CacheEvictConfig cacheEvictConfig = new CacheEvictConfig();
            cacheEvictConfig.setName(name);
            cacheEvictConfig.setKey(key);
            cacheEvictConfig.setInvalidate(invalidate);
            cacheEvictConfig.setInvocation(invocation);
            return cacheEvictConfig;
        }
    }
}
