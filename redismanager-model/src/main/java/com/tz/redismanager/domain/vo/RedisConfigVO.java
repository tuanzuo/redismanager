package com.tz.redismanager.domain.vo;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.validator.ValidGroup;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * redis连接配置VO
 *
 * @Since:2019-08-23 22:32:31
 * @Version:1.1.0
 */
@ScriptAssert.List({
        @ScriptAssert(lang = "javascript", groups = {ValidGroup.TestConnection.class},
                script = "_this.testConnectionValidate(_this.password,_this.source)", message = "source不能为空")
})
@Getter
@Setter
public class RedisConfigVO {

    @NotEmpty(message = "id不能为空", groups = {ValidGroup.UpdateConnection.class})
    private String id;

    /**
     * 名称
     */
    @NotEmpty(message = "名称不能为空", groups = {ValidGroup.AddConnection.class, ValidGroup.UpdateConnection.class})
    private String name;

    /**
     * 是否公开[1=是,0=否] v1.5.0
     * @see ConstInterface.IS_PUBLIC
     */
    @NotNull(message = "是否公开不能为空", groups = {ValidGroup.AddConnection.class, ValidGroup.UpdateConnection.class})
    @Range(min = 0, max = 1, message = "是否公开只能为0或者1", groups = {ValidGroup.AddConnection.class, ValidGroup.UpdateConnection.class})
    private Integer isPublic;

    /**
     * 类型[1=单机,2=集群]
     *
     * @see ConstInterface.TYPE
     */
    @NotNull(message = "类型不能为空", groups = {ValidGroup.TestConnection.class, ValidGroup.AddConnection.class, ValidGroup.UpdateConnection.class})
    @Range(min = 1, max = 2, message = "类型只能为1或者2", groups = {ValidGroup.TestConnection.class, ValidGroup.AddConnection.class, ValidGroup.UpdateConnection.class})
    private Integer type;

    /**
     * 地址
     * 单机-->127.0.0.1:6379
     * 集群-->192.168.1.32:7000,192.168.1.32:7001,192.168.1.32:7002,192.168.1.32:7003,192.168.1.32:7004,192.168.1.32:7005
     */
    @NotEmpty(message = "地址不能为空", groups = {ValidGroup.TestConnection.class, ValidGroup.AddConnection.class, ValidGroup.UpdateConnection.class})
    private String address;

    /**
     * 密码
     */
    private String password;

    /**
     * 序列化代码
     */
    private String serCode;

    /**
     * 备注
     */
    private String note;

    /**
     * 创建人
     */
    private String creater;


    /**
     * 修改人
     */
    private String updater;

    /**
     * 来源：1添加，2修改
     *
     * @see ConstInterface.SOURCE
     */
    @Range(min = 1, max = 2, message = "source只能为1或者2", groups = {ValidGroup.TestConnection.class})
    private Integer source;

    public boolean testConnectionValidate(String password, Integer source) {
        if (StringUtils.isNotBlank(password)) {
            if (null == source) {
                return false;
            }
        }
        return true;
    }

}