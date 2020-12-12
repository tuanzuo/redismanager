package com.tz.redismanager.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>角色列表返回对象</p>
 *
 * @author tuanzuo
 * @version 1.4.0
 * @time 2020-09-26 16:41
 **/
@Getter
@Setter
public class RoleListResp {

    private Pagination pagination;

    private List<RoleResp> list;

}
