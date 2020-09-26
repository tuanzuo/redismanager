package com.tz.redismanager.domain.vo;

import com.tz.redismanager.validator.ValidGroup;
import org.hibernate.validator.constraints.NotEmpty;

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
    private Integer id;

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
     * 备注
     */
    private String note;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}