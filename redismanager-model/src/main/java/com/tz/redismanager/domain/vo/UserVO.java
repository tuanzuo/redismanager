package com.tz.redismanager.domain.vo;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.validator.ValidGroup;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户VO
 *
 * @version 1.3.0
 * @time 2020-08-29 13:43
 **/
public class UserVO {

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空", groups = {ValidGroup.resetUserPwd.class, ValidGroup.grantUserRole.class,})
    private Integer id;

    /**
     * 用户id集合
     */
    @NotEmpty(message = "用户id集合不能为空", groups = {ValidGroup.updateUserStatus.class})
    private List<Integer> ids;

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空", groups = {ValidGroup.addUserInfo.class, ValidGroup.updateUserInfo.class})
    private String name;

    /**
     * 原用户名
     */
    private String oldName;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空", groups = {ValidGroup.addUserInfo.class, ValidGroup.updateUserPwd.class,})
    private String pwd;

    @NotEmpty(message = "原密码不能为空", groups = {ValidGroup.updateUserPwd.class,})
    private String oldPwd;

    /**
     * 状态
     * {@link ConstInterface.USER_STATUS}
     */
    @Range(min = 0, max = 1, message = "状态不能为空", groups = {ValidGroup.updateUserStatus.class})
    private Integer status;

    /**
     * 备注
     */
    private String note;

    /**
     * 角色id列表
     */
    private List<Integer> roleIds;

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

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
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

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }
}