package com.tz.redismanager.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>分页</p>
 *
 * @author tuanzuo
 * @version 1.0
 * @time 2020-09-26 16:42
 **/
@Getter
@Setter
public class Pagination {

    private Integer total;
    private Integer current;
    private Integer pageSize;

}
