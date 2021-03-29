package com.tz.redismanager.cacher.config;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>缓存器配置</p>
 *
 * @author tuanzuo
 * @version 1.6.1
 * @time 2021-03-29 22:16
 **/
@Getter
@Setter
public class CacherConfig {

    private String name;

    private String key;

    private boolean asyncRefresh = true;

    private L1CacheConfig l1Cache;

    private L2CacheConfig l2Cache;

    private CacherConfig() {

    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private String key;
        private boolean asyncRefresh = true;
        private L1CacheConfig l1Cache;
        private L2CacheConfig l2Cache;

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

        public Builder setAsyncRefresh(boolean asyncRefresh) {
            this.asyncRefresh = asyncRefresh;
            return this;
        }

        public Builder setL1Cache(L1CacheConfig l1Cache) {
            this.l1Cache = l1Cache;
            return this;
        }

        public Builder setL2Cache(L2CacheConfig l2Cache) {
            this.l2Cache = l2Cache;
            return this;
        }

        public CacherConfig build() {
            CacherConfig cacherConfig = new CacherConfig();
            cacherConfig.setName(name);
            cacherConfig.setKey(key);
            cacherConfig.setAsyncRefresh(asyncRefresh);
            cacherConfig.setL1Cache(l1Cache);
            cacherConfig.setL2Cache(l2Cache);
            return cacherConfig;
        }
    }
}
