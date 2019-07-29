package com.tz.redismanager.service.impl;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.tz.redismanager.bean.ApiResult;
import com.tz.redismanager.bean.ResultCode;
import com.tz.redismanager.bean.po.RedisConfigPO;
import com.tz.redismanager.bean.vo.RedisConfigVO;
import com.tz.redismanager.config.EncryptConfig;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.mapper.RedisConfigPOMapper;
import com.tz.redismanager.service.IRedisContextService;
import com.tz.redismanager.util.RSAUtil;
import com.tz.redismanager.util.RedisContextUtils;
import com.tz.redismanager.util.RsaException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class RedisContextServiceImpl implements IRedisContextService, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(RedisContextServiceImpl.class);

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
    public RedisTemplate<String, Object> initContext(String id) {
        logger.info("[redisContext] [initContext] {正在初始化redisTemplate,id:{}}", id);
        if (StringUtils.isBlank(id)) {
            logger.error("[redisContext] [initContext] [id为空]");
            return null;
        }
        if (redisTemplateMap.containsKey(id)) {
            logger.info("[redisContext] [initContext] [已存在对应的redisTemplate] {id:{}}", id);
            return null;
        }
        RedisConfigPO redisConfigPO =  this.getRedisConfigCache().get(id);
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
            if(StringUtils.isNotBlank(redisConfigPO.getPassword())){
                passwrod = RSAUtil.rsaPrivateDecrypt(redisConfigPO.getPassword(), encryptConfig.getPrivateKey(), RSAUtil.CHARSET_UTF8);
            }
            redisTemplate = RedisContextUtils.initRedisTemplate(redisConfigPO.getType(), redisConfigPO.getAddress(), passwrod);
            passwrod = null;
        } catch (UnknownHostException e) {
            logger.error("[redisContext] [initContext] [初始化redisTemplate出错] {redisConfigPO:{}}", redisConfigPO, e);
            return redisTemplate;
        } catch (RsaException e) {
            logger.error("[redisContext] [initContext] [解密失败] {redisConfigPO:{}}", redisConfigPO, e);
        }
        if (null != redisTemplate && StringUtils.isNotBlank(redisConfigPO.getSerCode())) {
            RedisContextUtils.initRedisSerializer(redisConfigPO.getSerCode(), redisTemplate);
        }
        redisTemplateMap.put(id, redisTemplate);
        logger.info("[redisContext] [initContext] {初始化redisTemplate完成,id:{}}", id);
        return redisTemplate;
    }

    @Override
    public RedisTemplate<String, Object> getRedisTemplate(String id) {
        return redisTemplateMap.get(id);
    }

    @Override
    public Map<String, RedisTemplate<String, Object>> getRedisTemplateMap(){
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
            String passwrod = null;
            if (StringUtils.isNotBlank(vo.getPassword())) {
                if (null != vo.getSource() && ConstInterface.SOURCE.UPDATE.intValue() == vo.getSource()) {
                    try {
                        passwrod = RSAUtil.rsaPrivateDecrypt(vo.getPassword(), encryptConfig.getPrivateKey(), RSAUtil.CHARSET_UTF8);
                    } catch (Exception e) {
                        passwrod = vo.getPassword();
                    }
                } else {
                    passwrod = vo.getPassword();
                }
            }
            redisTemplate = RedisContextUtils.initRedisTemplate(vo.getType(), vo.getAddress(), passwrod);
            passwrod = null;
            if (null != redisTemplate && StringUtils.isNotBlank(vo.getSerCode())) {
                RedisContextUtils.initRedisSerializer(vo.getSerCode(), redisTemplate);
            }
            redisTemplate.randomKey();
            result = new ApiResult<>(ResultCode.SUCCESS);
        } catch (Throwable e) {
            Throwable old = e;
            Throwable cause = e.getCause();
            while (null != cause) {
                old = cause;
                cause = cause.getCause();
            }
            result.setMsg(old.getMessage());
            logger.error("[redisContext] [testRedisConnection] {param:{},测试连接失败}", vo, e);
        }
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LoadingCache<String, RedisConfigPO> redisConfigCache = Caffeine.newBuilder()
                .expireAfterAccess(1L, TimeUnit.HOURS)
                .initialCapacity(10)
                .maximumSize(1000)
                .build((id) -> {
                    logger.info("[回源查询] {id:{}对应的redis连接信息}", id);
                    return redisConfigPOMapper.selectByPrimaryKey(id);
                });
        cacheMap.put(ConstInterface.Cacher.REDIS_CONFIG_CACHER, redisConfigCache);
        logger.info("[初始化缓存] {{}完成}", ConstInterface.Cacher.REDIS_CONFIG_CACHER);
    }


}
