package com.tz.redismanager.dao.domain.dto;

import com.tz.redismanager.dao.domain.po.RolePO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-13 0:14
 **/
@Getter
@Setter
@ToString
public class RoleDTO extends RolePO {

    /**
     * 用户Id
     */
    private Integer userId;
}
