package com.tz.redismanager.validator;

/**
 * 验证分组接口
 *
 * @Since:2019-08-23 22:55:52
 * @Version:1.1.0
 */
public interface ValidGroup {
    /**
     * 测试Redis连接
     */
    interface TestConnection {
    }

    /**
     * 添加Redis连接
     */
    interface AddConnection {
    }

    /**
     * 修改Redis连接
     */
    interface UpdateConnection {
    }

    /**
     * 修改key名称
     */
    interface RenameKey {
    }

    /**
     * 修改TTL
     */
    interface SetTTL {
    }

    /**
     * 修改key的value
     */
    interface UpdateKeyValue {
    }

    /**
     * 添加用户信息
     */
    interface AddUserInfo {
    }

    /**
     * 修改用户信息
     */
    interface UpdateUserInfo {
    }

    /**
     * 修改用户密码
     */
    interface UpdateUserPwd {
    }

    /**
     * 重置用户密码
     */
    interface ResetUserPwd {
    }

    /**
     * 修改用户状态
     */
    interface UpdateUserStatus {
    }

    /**
     * 用户分配角色
     */
    interface GrantUserRole {
    }

    /**
     * 添加角色信息
     */
    interface AddRole {
    }

    /**
     * 修改角色信息
     */
    interface UpdateRole {
    }

    /**
     * 修改角色状态
     */
    interface UpdateRoleStatus {
    }

    /**
     * 登录
     */
    interface Login {
    }

    /**
     * 添加配置信息
     */
    interface AddConfig {
    }

    /**
     * 修改配置信息
     */
    interface UpdateConfig {
    }

    /**
     * 删除配置信息
     */
    interface DelConfig {
    }



}