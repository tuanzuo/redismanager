package com.tz.redismanager.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>用户列表返回对象</p>
 *
 * @author tuanzuo
 * @version 1.4.0
 * @time 2020-09-26 16:41
 **/
@Getter
@Setter
public class UserListResp {

    /**
     * 分页对象
     */
    private Pagination pagination;

    /**
     * 用户列表
     */
    private List<UserResp> list;

    /**
     * 角色列表
     */
    private List<RoleVO> roles;

}
