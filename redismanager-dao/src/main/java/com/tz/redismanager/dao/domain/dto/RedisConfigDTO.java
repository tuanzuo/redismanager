package com.tz.redismanager.dao.domain.dto;

import com.tz.redismanager.dao.domain.po.RedisConfigExtPO;
import com.tz.redismanager.dao.domain.po.RedisConfigPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <p>redis连接配置DTO</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-06-06 13:52
 **/
@Getter
@Setter
@ToString
public class RedisConfigDTO extends RedisConfigPO {

    /**
     * 扩展List
     */
    private List<RedisConfigExtPO> extList;
}
