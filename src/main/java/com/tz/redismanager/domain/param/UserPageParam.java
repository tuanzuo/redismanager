package com.tz.redismanager.domain.param;

import com.tz.redismanager.constant.ConstInterface;

/**
 * <p>用户分页param</p>
 *
 * @author Administrator
 * @version 1.4.0
 * @time 2020-10-02 13:28
 **/
public class UserPageParam {

    private static final Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * 用户名
     */
    private String name;
    /**
     * 状态
     * {@link ConstInterface.USER_STATUS}
     */
    private Integer status;

    /**
     * 当前页数
     */
    private Integer currentPage = 1;
    /**
     * 每页条数
     */
    private Integer pageSize = DEFAULT_PAGE_SIZE;

    public int getOffset() {
        //有参数处理逻辑
        return (this.getCurrentPage() - 1) * this.getPageSize();
    }

    public int getRows() {
        //有参数处理逻辑
        return this.getPageSize();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        //有参数处理逻辑
        if (null == currentPage || currentPage <= 0) {
            currentPage = 1;
        }
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        //有参数处理逻辑
        if (null == pageSize || pageSize <= 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        this.pageSize = pageSize;
    }

}
