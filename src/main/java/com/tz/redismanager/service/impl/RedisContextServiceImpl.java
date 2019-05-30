package com.tz.redismanager.service.impl;

import com.tz.redismanager.bean.po.RedisConfigPO;
import com.tz.redismanager.dao.mapper.RedisConfigPOMapper;
import com.tz.redismanager.service.IRedisContextService;
import com.tz.redismanager.util.RedisContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RedisContextServiceImpl implements IRedisContextService {

    private static final Logger logger = LoggerFactory.getLogger(RedisContextServiceImpl.class);

    private static Map<String, RedisTemplate<String, Object>> redisTemplateMap = new ConcurrentHashMap<>();

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
        RedisConfigPO redisConfigPO = redisConfigPOMapper.selectByPrimaryKey(id);
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
            redisTemplate = RedisContextUtils.initRedisTemplate(redisConfigPO.getType(), redisConfigPO.getAddress(), redisConfigPO.getPassword());
        } catch (UnknownHostException e) {
            logger.error("[redisContext] [initContext] [初始化redisTemplate出错] {redisConfigPO:{}}", redisConfigPO, e);
            return redisTemplate;
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

}
