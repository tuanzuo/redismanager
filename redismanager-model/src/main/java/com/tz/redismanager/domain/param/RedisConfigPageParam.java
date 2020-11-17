package com.tz.redismanager.domain.param;

import com.tz.redismanager.constant.ConstInterface;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>Redis配置信息分页param</p>
 *
 * @author Administrator
 * @version 1.4.0
 * @time 2020-10-02 13:42
 **/
public class RedisConfigPageParam {

    private static final Integer DEFAULT_PAGE_SIZE = 9;

    private String searchKey;

    /**
     * 是否公开[1=是,0=否] v1.5.0
     * @see ConstInterface.IS_PUBLIC
     */
    private Integer isPublic;

    /**
     * 用户名 v1.5.0
     */
    private String userName;

    /**
     * 是否是超级管理员
     * @see ConstInterface.IS_SUPER_ADMIN
     */
    private Integer isSuperAdmin = ConstInterface.IS_SUPER_ADMIN.NO;

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

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getIsSuperAdmin() {
        return isSuperAdmin;
    }

    public void setIsSuperAdmin(Integer isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
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
