package com.tz.redismanager.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>分页列表返回对象</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-22 21:07
 **/
@Getter
@Setter
public class PageResp<T> {

    /**
     * 分页对象
     */
    private Pagination pagination;

    /**
     * 数据集合
     */
    private List<T> list;

}
