package com.tz.redismanager.strategy.queryvalue;

import com.tz.redismanager.annotation.StrategyType;
import com.tz.redismanager.domain.dto.GroovyRunDTO;
import com.tz.redismanager.domain.vo.RedisValueQueryVO;
import com.tz.redismanager.enm.StrategyTypeEnum;
import com.tz.redismanager.service.IRedisContextService;
import com.tz.redismanager.strategy.AbstractHandler;
import com.tz.redismanager.util.CommonUtils;
import com.tz.redismanager.util.RedisContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 抽象的查询redis key的value处理器
 *
 * @author tuanzuo
 * @version 1.0
 * @time 2019-06-23 21:40
 **/
@StrategyType({StrategyTypeEnum.QUERY_VALUE})
public abstract class AbstractQueryValueHandler extends AbstractHandler<RedisValueQueryVO, Object> implements IQueryValueHandler {

    @Autowired
    private IRedisContextService redisContextService;

    /**
     * 重新设置keySerializer
     *
     * @param redisTemplate
     */
    public void reSetKeySerializer(RedisTemplate<String, Object> redisTemplate) {
        String serializeCode = redisContextService.getRedisTemplateSerCode(RedisContextUtils.getRedisConfigId());
        if (StringUtils.isNotBlank(serializeCode)) {
            GroovyRunDTO dto = RedisContextUtils.initRedisSerializerCommon(serializeCode, redisTemplate, 2);
            if (dto.getHitSerializerCategory()) {
                return;
            }
        }
        CommonUtils.reSetKeySerializer(redisTemplate);
    }
}
