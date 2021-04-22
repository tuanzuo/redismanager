package com.tz.redismanager.config.domain.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>配置分页查询参数</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-11 22:05
 **/
@Getter
@Setter
@ToString
public class ConfigPageParam extends ConfigQueryParam {

    public static final Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * 当前页数
     */
    private Integer currentPage = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = DEFAULT_PAGE_SIZE;

    private Integer offset;

    private Integer rows;
}
