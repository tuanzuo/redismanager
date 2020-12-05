package com.tz.redismanager.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * redis key对应value的返回数据
 *
 * @Since:2019-08-23 22:38:20
 * @Version:1.1.0
 */
@Setter
@Getter
public class RedisValueResp {
    /**
     * key类型：string,List,set,hash,zset
     */
    private String keyType;
    /**
     * 过期时间
     */
    private Long expireTime;
    /**
     * key对应的value
     */
    private Object value;
    /**
     * 当前value对应的页数
     */
    private Integer pageNum;
    /**
     * 开始位置-list类型修改使用
     */
    private long start;
    /**
     * 结束位置-list类型修改使用
     */
    private long end;

}
