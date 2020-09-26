package com.tz.redismanager.domain.vo;

/**
 * <p>分页</p>
 *
 * @author Administrator
 * @version 1.0
 * @time 2020-09-26 16:42
 **/
public class Pagination {

    private Integer total;
    private Integer current;
    private Integer pageSize;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
