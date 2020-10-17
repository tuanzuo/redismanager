package com.tz.redismanager.service.impl;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.tz.redismanager.annotation.MethodLog;
import com.tz.redismanager.config.EncryptConfig;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.mapper.RedisConfigPOMapper;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.dao.domain.po.RedisConfigPO;
import com.tz.redismanager.domain.vo.RedisConfigVO;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.service.IRedisContextService;
import com.tz.redismanager.trace.TraceLoggerFactory;
import com.tz.redismanager.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class RedisContextServiceImpl implements IRedisContextService, InitializingBean {

    private static final Logger logger = TraceLoggerFactory.getLogger(RedisContextServiceImpl.class);

    /**
     * RedisTemplate缓存Map--key:id,value:RedisTemplate
     */
    private static Map<String, RedisTemplate<String, Object>> redisTemplateMap = new ConcurrentHashMap<>();

    private static Map<String, LoadingCache> cacheMap = new ConcurrentHashMap<>();

    @Autowired
    private EncryptConfig encryptConfig;
    @Autowired
    private RedisConfigPOMapper redisConfigPOMapper;

    @Override
    @MethodLog(logPrefix = "RedisTemplate初始化", logInputParams = false, logOutputParams = false)
    public RedisTemplate<String, Object> initContext(String id) {
        if (redisTemplateMap.containsKey(id)) {
            logger.info("[redisContext] [initContext] [已存在对应的redisTemplate] {id:{}}", id);
            return null;
        }
        RedisConfigPO redisConfigPO = this.getRedisConfigCache().get(id);
        if (null == redisConfigPO) {
            logger.error("[redisContext] [initContext] [查询不到redisConfig数据] {id:{}}", id);
            return null;
        }
        if (StringUtils.isBlank(redisConfigPO.getAddress())) {
            logger.error("[redisContext] [initContext] [redisConfig配置的地址为空] {redisConfigPO:{}}", redisConfigPO);
            return null;
        }
        RedisTemplate<String, Object> redisTemplate = null;
        try {
            String passwrod = null;
            if (StringUtils.isNotBlank(redisConfigPO.getPassword())) {
                passwrod = RSAUtils.rsaPrivateDecrypt(redisConfigPO.getPassword(), encryptConfig.getPrivateKey(), RSAUtils.CHARSET_UTF8);
            }
            redisTemplate = RedisContextUtils.initRedisTemplate(redisConfigPO.getType(), redisConfigPO.getAddress(), passwrod);
            passwrod = null;
        } catch (RsaException e) {
            logger.error("[redisContext] [initContext] [解密失败] {redisConfigPO:{}}", redisConfigPO, e);
            return redisTemplate;
        } catch (Exception e) {
            logger.error("[redisContext] [initContext] [初始化initRedisTemplate出错] {redisConfigPO:{}}", redisConfigPO, e);
            return redisTemplate;
        }
        if (null != redisTemplate && StringUtils.isNotBlank(redisConfigPO.getSerCode())) {
            RedisContextUtils.initRedisSerializer(redisConfigPO.getSerCode(), redisTemplate);
        }
        if (null != redisTemplate) {
            redisTemplateMap.put(id, redisTemplate);
            logger.info("[redisContext] [initContext] {初始化redisTemplate完成,id:{}}", id);
        } else {
            logger.info("[redisContext] [initContext] {初始化redisTemplate失败,id:{}}", id);
        }
        return redisTemplate;
    }

    @Override
    public RedisTemplate<String, Object> getRedisTemplate(String id) {
        return redisTemplateMap.get(id);
    }

    @Override
    public Map<String, RedisTemplate<String, Object>> getRedisTemplateMap() {
        return redisTemplateMap;
    }

    @Override
    public void removeRedisTemplate(String id) {
        redisTemplateMap.remove(id);
        logger.info("[redisContext] [removeRedisTemplate] {redisTemplate清理完成,id:{}}", id);
    }

    @Override
    public LoadingCache<String, RedisConfigPO> getRedisConfigCache() {
        return cacheMap.get(ConstInterface.Cacher.REDIS_CONFIG_CACHER);
    }

    @Override
    public ApiResult<String> testRedisConnection(RedisConfigVO vo) {
        ApiResult<String> result = new ApiResult<>(ResultCode.FAIL);
        try {
            RedisTemplate<String, Object> redisTemplate = null;
            String passwrod = this.handleRedisPassword(vo);
            redisTemplate = RedisContextUtils.initRedisTemplate(vo.getType(), vo.getAddress(), passwrod);
            passwrod = null;
            if (null != redisTemplate && StringUtils.isNotBlank(vo.getSerCode())) {
                RedisContextUtils.initRedisSerializer(vo.getSerCode(), redisTemplate);
            }
            String key = "rds:mag:" + UUIDUtils.generateId();
            redisTemplate.opsForValue().set(key, key, 5, TimeUnit.SECONDS);
            result = new ApiResult<>(ResultCode.SUCCESS);
        } catch (Throwable e) {
            result.setMsg(CommonUtils.getExcpMsg(e));
            logger.error("[redisContext] [testRedisConnection] {param:{},测试连接失败}", vo, e);
        }
        return result;
    }

    private String handleRedisPassword(RedisConfigVO vo) {
        String passwrod = null;
        if (StringUtils.isBlank(vo.getPassword())) {
            return passwrod;
        }
        if (null != vo.getSource() && ConstInterface.SOURCE.UPDATE.intValue() == vo.getSource()) {
            try {
                passwrod = RSAUtils.rsaPrivateDecrypt(vo.getPassword(), encryptConfig.getPrivateKey(), RSAUtils.CHARSET_UTF8);
            } catch (Exception e) {
                passwrod = vo.getPassword();
            }
        } else {
            passwrod = vo.getPassword();
        }
        return passwrod;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LoadingCache<String, RedisConfigPO> redisConfigCache = Caffeine.newBuilder()
                //最后一次访问了之后多久过期
                .expireAfterAccess(1L, TimeUnit.HOURS)
                .initialCapacity(10)
                .maximumSize(1000)
                .build((id) -> {
                    logger.info("[本地缓存] [{}] [回源查询] {id:{}对应的redis连接信息}", ConstInterface.Cacher.REDIS_CONFIG_CACHER, id);
                    return redisConfigPOMapper.selectByPrimaryKey(id);
                });
        cacheMap.put(ConstInterface.Cacher.REDIS_CONFIG_CACHER, redisConfigCache);
        logger.info("[本地缓存] [{}] [初始化完成]", ConstInterface.Cacher.REDIS_CONFIG_CACHER);
    }


}
