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
 * 用户VO
 *
 * @version 1.3.0
 * @time 2020-08-29 13:43
 **/
@Getter
@Setter
public class UserVO extends CaptchaVO{

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空", groups = {ValidGroup.ResetUserPwd.class, ValidGroup.GrantUserRole.class,})
    private Integer id;

    /**
     * 用户id集合
     */
    @NotEmpty(message = "用户id集合不能为空", groups = {ValidGroup.UpdateUserStatus.class})
    private List<Integer> ids;

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空", groups = {ValidGroup.AddUserInfo.class, ValidGroup.UpdateUserInfo.class})
    private String name;

    /**
     * 原用户名
     */
    private String oldName;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空", groups = {ValidGroup.AddUserInfo.class, ValidGroup.UpdateUserPwd.class,})
    private String pwd;

    @NotEmpty(message = "原密码不能为空", groups = {ValidGroup.UpdateUserPwd.class,})
    private String oldPwd;

    /**
     * 状态
     * {@link ConstInterface.USER_STATUS}
     */
    @Range(min = 0, max = 1, message = "状态不能为空", groups = {ValidGroup.UpdateUserStatus.class})
    private Integer status;

    /**
     * 备注
     */
    private String note;

    /**
     * 角色id列表
     */
    private List<Integer> roleIds;


}