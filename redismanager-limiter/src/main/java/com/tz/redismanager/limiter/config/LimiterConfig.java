package com.tz.redismanager.limiter.config;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * <p>限流器配置</p>
 *
 * @author tuanzuo
 * @version 1.6.1
 * @time 2021-03-30 21:56
 **/
@Getter
@Setter
public class LimiterConfig {

    /**
     * 限流器名称
     *
     * @return
     */
    private String name;

    /**
     * 限流器key
     */
    private String key;

    /**
     * qps
     */
    private double qps;

    /**
     * 获得许可的数量，默认为1
     */
    private int permits = 1;

    /**
     * 获得许可的超时时间，默认为0(不超时)
     */
    private long timeout = 0;

    /**
     * 获得许可的超时时间的单位，默认为TimeUnit.MICROSECONDS
     */
    private TimeUnit unit = TimeUnit.MICROSECONDS;

    private LimiterConfig(){

    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private String key;
        private double qps;
        private int permits = 1;
        private long timeout = 0;
        private TimeUnit unit = TimeUnit.MICROSECONDS;

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

        public Builder setQps(double qps) {
            this.qps = qps;
            return this;
        }

        public Builder setPermits(int permits) {
            this.permits = permits;
            return this;
        }

        public Builder setTimeout(long timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder setUnit(TimeUnit unit) {
            this.unit = unit;
            return this;
        }

        public LimiterConfig build() {
            LimiterConfig limiterConfig = new LimiterConfig();
            limiterConfig.setName(name);
            limiterConfig.setKey(key);
            limiterConfig.setQps(qps);
            limiterConfig.setPermits(permits);
            limiterConfig.setTimeout(timeout);
            limiterConfig.setUnit(unit);
            return limiterConfig;
        }
    }
}
