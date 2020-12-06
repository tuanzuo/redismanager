package com.tz.redismanager.domain.vo;

import com.tz.redismanager.annotation.ConnectionId;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * redis key查询value的VO
 * @Since:2019-08-23 22:35:31
 * @Version:1.1.0
 */
@Setter
public class RedisValueQueryVO {

    private static final Integer DEFAULT_PAGE_NUM = 1;
    private static final Integer DEFAULT_PAGE_SIZE = 1000;

    @NotEmpty(message = "id不能为空")
    @ConnectionId
    private String id;
    @NotEmpty(message = "searchKey不能为空")
    private String searchKey;
    /**
     * 查询的页数
     */
    private Integer pageNum = DEFAULT_PAGE_NUM;
    /**
     * 每页大小
     */
    private Integer pageSize = DEFAULT_PAGE_SIZE;
    /**
     * 开始位置
     */
    private long start;
    /**
     * 结束位置
     */
    private long end;

    public String getId() {
        return id;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public Integer getPageNum() {
        //有参数处理逻辑
        return (null == pageNum || pageNum <= 0) ? DEFAULT_PAGE_NUM : pageNum;
    }

    public Integer getPageSize() {
        //有参数处理逻辑
        return (null == pageSize || pageSize <= 0) ? DEFAULT_PAGE_SIZE : pageSize;
    }

    /**
     * 得到开始位置
     */
    public long getStart() {
        //有参数处理逻辑
        return (this.getPageNum() - 1) * this.getPageSize();
    }
    /**
     * 得到结束位置
     */
    public long getEnd() {
        //有参数处理逻辑
        return (this.getPageNum() * this.getPageSize()) - 1;
    }
}
