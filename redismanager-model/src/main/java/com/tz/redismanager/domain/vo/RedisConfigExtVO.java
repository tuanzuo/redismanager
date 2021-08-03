package com.tz.redismanager.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-06-06 14:32
 **/
@Getter
@Setter
public class RedisConfigExtVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 扩展key
     */
    private String extKey;

    /**
     * 扩展名称
     */
    private String extName;

    /**
     * 扩展value
     */
    private String extValue;
}
