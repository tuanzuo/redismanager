package com.tz.redismanager.dao.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <p>redis连接配置统计数据DTO</p>
 *
 * @version 1.5.0
 * @time 2020-10-17 16:07
 **/
@Getter
@Setter
public class RedisConfigAnalysisDTO {

    private Date createTime;
    private Integer configCount;
}
