package com.tz.redismanager.domain.vo;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.validator.ValidGroup;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色VO
 *
 * @version 1.4.0
 * @time 2020-08-29 13:43
 **/
public class RoleVO {

    /**
     * 角色id
     */
    @NotNull(message = "角色id不能为空", groups = {ValidGroup.updateRole.class})
    private Integer id;

    /**
     * 角色id集合
     */
    @NotEmpty(message = "角色id集合不能为空", groups = {ValidGroup.updateRoleStatus.class})
    private List<Integer> ids;

    /**
     * 角色名称
     */
    @NotEmpty(message = "角色名称不能为空", groups = {ValidGroup.addRole.class, ValidGroup.updateRole.class})
    private String name;

    /**
     * 角色编码
     */
    @NotEmpty(message = "角色编码不能为空", groups = {ValidGroup.addRole.class, ValidGroup.updateRole.class})
    private String code;

    /**
     * 状态[1=启用,0=禁用]
     * {@link ConstInterface.ROLE_STATUS}
     */
    @Range(min = 0, max = 1, message = "状态不能为空", groups = {ValidGroup.addRole.class, ValidGroup.updateRole.class, ValidGroup.updateRoleStatus.class})
    private Integer status;

    /**
     * 备注
     */
    private String note;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}