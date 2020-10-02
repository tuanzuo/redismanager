package com.tz.redismanager.domain.param;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>Redis配置信息分页param</p>
 *
 * @author Administrator
 * @version 1.0
 * @time 2020-10-02 13:42
 **/
public class RedisConfigPageParam {

    private static final Integer DEFAULT_PAGE_SIZE = 9;

    private String searchKey;
    /**
     * 查询的页数
     */
    private Integer pageNum = 1;
    /**
     * 每页大小
     */
    private Integer pagesize = DEFAULT_PAGE_SIZE;

    public int getOffset() {
        //有参数处理逻辑
        return (this.getPageNum() - 1) * this.getPagesize();
    }

    public int getRows() {
        //有参数处理逻辑
        return this.getPagesize();
    }

    public String getSearchKey() {
        //有参数处理逻辑
        if (StringUtils.isBlank(searchKey)) {
            searchKey = null;
        } else {
            searchKey = searchKey.trim();
        }
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        if (null == pageNum || pageNum <= 0) {
            pageNum = 1;
        }
        this.pageNum = pageNum;
    }

    public Integer getPagesize() {
        return pagesize;
    }

    public void setPagesize(Integer pagesize) {
        if (null == pagesize || pagesize <= 0) {
            pagesize = DEFAULT_PAGE_SIZE;
        }
        this.pagesize = pagesize;
    }

}
