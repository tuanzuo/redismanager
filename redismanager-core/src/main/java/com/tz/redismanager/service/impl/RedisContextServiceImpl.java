package com.tz.redismanager.service.impl;

import com.tz.redismanager.annotation.MethodLog;
import com.tz.redismanager.config.EncryptConfig;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.domain.dto.RedisConfigDTO;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.RedisConfigVO;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.service.IRedisConfigService;
import com.tz.redismanager.service.IRedisContextService;
import com.tz.redismanager.trace.TraceLoggerFactory;
import com.tz.redismanager.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * redis上下文服务实现
 *
 * @author tuanzuo
 */
@Service
public class RedisContextServiceImpl implements IRedisContextService {

    private static final Logger logger = TraceLoggerFactory.getLogger(RedisContextServiceImpl.class);

    /**
     * RedisTemplate缓存Map--key:id,value:RedisTemplate
     */
    private static Map<String, RedisTemplate<String, Object>> redisTemplateMap = new ConcurrentHashMap<>();

    @Value("${upload.file.path.prefix}")
    private String uploadPrefix;

    @Autowired
    private IRedisConfigService redisConfigService;
    @Autowired
    private EncryptConfig encryptConfig;

    @Override
    @MethodLog(logPrefix = "RedisTemplate初始化", logInputParams = false, logOutputParams = false)
    public RedisTemplate<String, Object> initContext(String id) {
        if (redisTemplateMap.containsKey(id)) {
            logger.info("[redisContext] [initContext] [已存在对应的redisTemplate] {id:{}}", id);
            return null;
        }
        RedisConfigDTO redisConfigDto = redisConfigService.query(id);
        if (null == redisConfigDto) {
            logger.error("[redisContext] [initContext] [查询不到redisConfig数据] {id:{}}", id);
            return null;
        }
        if (StringUtils.isBlank(redisConfigDto.getAddress())) {
            logger.error("[redisContext] [initContext] [redisConfig配置的地址为空] {redisConfigPO:{}}", redisConfigDto);
            return null;
        }

        //加载jar
        if(CollectionUtils.isNotEmpty(redisConfigDto.getExtList())){
            redisConfigDto.getExtList().forEach(temp -> ClassLoaderUtils.loadJar(StringUtils.join(uploadPrefix, temp.getExtValue())));
        }

        RedisTemplate<String, Object> redisTemplate = null;
        try {
            String passwrod = null;
            if (StringUtils.isNotBlank(redisConfigDto.getPassword())) {
                passwrod = RSAUtils.rsaPrivateDecrypt(redisConfigDto.getPassword(), encryptConfig.getPrivateKey(), RSAUtils.CHARSET_UTF8);
            }
            //初始化RedisTemplate
            redisTemplate = RedisContextUtils.initRedisTemplate(redisConfigDto.getType(), redisConfigDto.getAddress(), passwrod);
            passwrod = null;
        } catch (RsaException e) {
            logger.error("[redisContext] [initContext] [解密失败] {redisConfigPO:{}}", redisConfigDto, e);
            return redisTemplate;
        } catch (Exception e) {
            logger.error("[redisContext] [initContext] [初始化initRedisTemplate出错] {redisConfigPO:{}}", redisConfigDto, e);
            return redisTemplate;
        }
        //设置自定义的序列化方式
        if (null != redisTemplate && StringUtils.isNotBlank(redisConfigDto.getSerCode())) {
            RedisContextUtils.initRedisSerializer(redisConfigDto.getSerCode(), redisTemplate);
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
            String key = StringUtils.join("rds:mag:", UUIDUtils.generateId());
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
}
