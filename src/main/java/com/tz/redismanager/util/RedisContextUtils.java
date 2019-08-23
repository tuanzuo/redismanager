package com.tz.redismanager.util;

import com.alibaba.fastjson.parser.ParserConfig;
import com.tz.redismanager.config.CustomRedisAutoConfiguration;
import com.tz.redismanager.constant.ConstInterface;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * RedisContext工具类
 *
 * @Since:2019-08-23 22:52:57
 * @Version:1.1.0
 */
public class RedisContextUtils {

    private static final Logger logger = LoggerFactory.getLogger(RedisContextUtils.class);

    public static ThreadLocal<RedisTemplate<String, Object>> threadLocalRedisTemplate = new InheritableThreadLocal<>();

    public static RedisTemplate<String, Object> getRedisTemplate() {
        return threadLocalRedisTemplate.get();
    }

    public static void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        threadLocalRedisTemplate.set(redisTemplate);
    }

    public static void cleanRedisTemplate() {
        threadLocalRedisTemplate.remove();
    }


    public static RedisTemplate<String, Object> initRedisTemplate(Integer type, String address, String password) throws UnknownHostException {
        RedisProperties redisProperties = new RedisProperties();
        //单机
        if (null == type || ConstInterface.TYPE.SINGLE.equals(type)) {
            logger.info("[RedisContextUtils] [initRedisTemplate] {初始化单机redisTemplate}");
            String[] addressArray = StringUtils.split(address, ConstInterface.Symbol.COLON);
            if (ArrayUtils.isEmpty(addressArray)) {
                logger.error("[RedisContextUtils] [initRedisTemplate] {不能初始化redisTemplate,address格式错误,type:{},address:{}}", type, address);
                return null;
            }
            if (addressArray.length == 2) {
                redisProperties.setHost(addressArray[0].trim());
                redisProperties.setPort(Integer.parseInt(addressArray[1].trim()));
            } else if (addressArray.length == 1) {
                redisProperties.setHost(addressArray[0].trim());
            } else {
                logger.error("[RedisContextUtils] [initRedisTemplate] {不能初始化redisTemplate,address格式错误,type:{},address:{}}", type, address);
                return null;
            }
        } else if (ConstInterface.TYPE.CLUSTER.equals(type)) {//集群
            logger.info("[RedisContextUtils] [initRedisTemplate] {初始化集群redisTemplate}");
            RedisProperties.Cluster cluster = new RedisProperties.Cluster();
            String nodes = address;
            cluster.setNodes(Arrays.asList(nodes.split(",")));
            cluster.setMaxRedirects(2);
            redisProperties.setCluster(cluster);
        }
        //设置密码
        if (StringUtils.isNotBlank(password)) {
            redisProperties.setPassword(password.trim());
        }

        CustomRedisAutoConfiguration.RedisConnectionConfiguration redisConnectionConfiguration = new
                CustomRedisAutoConfiguration.RedisConnectionConfiguration(redisProperties, null, null);
        JedisConnectionFactory redisConnectionFactory = redisConnectionConfiguration.redisConnectionFactory();
        redisConnectionFactory.afterPropertiesSet();

        RedisTemplate<String, Object> customRedisTemplate = new RedisTemplate<>();
        customRedisTemplate.setConnectionFactory(redisConnectionFactory);

        //设置key,hashKey,value,hashValue的序列化方式
        customRedisTemplate.setKeySerializer(new StringRedisSerializer());
        customRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
        customRedisTemplate.setValueSerializer(new StringRedisSerializer());
        customRedisTemplate.setHashValueSerializer(new StringRedisSerializer());

        //建议使用这种方式，小范围指定白名单
        ParserConfig.getGlobalInstance().addAccept("com.tz.redismanager.domain");
        //设置了白名单才能正常从缓存中序列化出来
        ParserConfig.getGlobalInstance().addAccept("com.tz.redismanager.domain.po");

        customRedisTemplate.afterPropertiesSet();
        return customRedisTemplate;
    }

    public static void initRedisSerializer(String serializeCode, RedisTemplate<String, Object> redisTemplate) {
        Binding binding = new Binding();
        binding.setVariable("customRedisTemplate", redisTemplate);

        GroovyClassLoader groovyClassLoader = new GroovyClassLoader(RedisContextUtils.class.getClassLoader());
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
        compilerConfiguration.setSourceEncoding("utf-8");
        compilerConfiguration.setScriptBaseClass("CustomRedisConfig");

        GroovyShell groovyShell = new GroovyShell(groovyClassLoader, binding, compilerConfiguration);
        Script script = groovyShell.parse(serializeCode);
        script.run();
    }
}
