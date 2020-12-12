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

    private Pagination pagination;

    private List<UserResp> list;

    private List<RoleVO> roles;

}
