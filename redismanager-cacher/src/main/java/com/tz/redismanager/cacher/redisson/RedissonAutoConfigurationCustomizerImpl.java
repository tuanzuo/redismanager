package com.tz.redismanager.cacher.redisson;

import org.apache.commons.lang3.StringUtils;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * <p>自定义Redisson的配置</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2021-01-05 23:21
 **/
@Service
public class RedissonAutoConfigurationCustomizerImpl implements RedissonAutoConfigurationCustomizer {

    private static final String GET_SENTINEL_SERVERS_CONFIG = "getSentinelServersConfig";
    private static final String GET_CLUSTER_SERVERS_CONFIG = "getClusterServersConfig";
    private static final String GET_SINGLE_SERVER_CONFIG = "getSingleServerConfig";

    @Override
    public void customize(Config configuration) {
        //当密码为空字符串时设置为null，否则启动会报错
        SentinelServersConfig sentinelServersConfig = (SentinelServersConfig) this.getServersConfig(GET_SENTINEL_SERVERS_CONFIG, configuration);
        if (null != sentinelServersConfig) {
            String pwd = sentinelServersConfig.getPassword();
            sentinelServersConfig.setPassword(StringUtils.isBlank(pwd) ? null : pwd);
        }
        ClusterServersConfig clusterServersConfig = (ClusterServersConfig) this.getServersConfig(GET_CLUSTER_SERVERS_CONFIG, configuration);
        if (null != clusterServersConfig) {
            String pwd = clusterServersConfig.getPassword();
            clusterServersConfig.setPassword(StringUtils.isBlank(pwd) ? null : pwd);
        }
        SingleServerConfig singleServerConfig = (SingleServerConfig) this.getServersConfig(GET_SINGLE_SERVER_CONFIG, configuration);
        if (null != singleServerConfig) {
            String pwd = singleServerConfig.getPassword();
            singleServerConfig.setPassword(StringUtils.isBlank(pwd) ? null : pwd);
        }
    }

    public Object getServersConfig(String methodName, Object target) {
        Method method = ReflectionUtils.findMethod(Config.class, methodName);
        ReflectionUtils.makeAccessible(method);
        Object result = ReflectionUtils.invokeMethod(method, target);
        method.setAccessible(false);
        return result;
    }
}
