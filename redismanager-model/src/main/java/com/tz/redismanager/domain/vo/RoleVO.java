package com.tz.redismanager.domain.vo;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.validator.ValidGroup;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色VO
 *
 * @author tuanzuo
 * @version 1.4.0
 * @time 2020-08-29 13:43
 **/
@Getter
@Setter
public class RoleVO {

    /**
     * 角色id
     */
    @NotNull(message = "角色id不能为空", groups = {ValidGroup.UpdateRole.class})
    private Integer id;

    /**
     * 角色id集合
     */
    @NotEmpty(message = "角色id集合不能为空", groups = {ValidGroup.UpdateRoleStatus.class})
    private List<Integer> ids;

    /**
     * 角色名称
     */
    @NotEmpty(message = "角色名称不能为空", groups = {ValidGroup.AddRole.class, ValidGroup.UpdateRole.class})
    private String name;

    /**
     * 角色编码
     */
    @NotEmpty(message = "角色编码不能为空", groups = {ValidGroup.AddRole.class, ValidGroup.UpdateRole.class})
    private String code;

    /**
     * 状态[1=启用,0=禁用]
     * {@link ConstInterface.ROLE_STATUS}
     */
    @Range(min = 0, max = 1, message = "状态不能为空", groups = {ValidGroup.AddRole.class, ValidGroup.UpdateRole.class, ValidGroup.UpdateRoleStatus.class})
    private Integer status;

    /**
     * 备注
     */
    private String note;

}