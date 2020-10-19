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
    interface addUserInfo {
    }

    /**
     * 修改用户信息
     */
    interface updateUserInfo {
    }

    /**
     * 修改用户密码
     */
    interface updateUserPwd {
    }

    /**
     * 重置用户密码
     */
    interface resetUserPwd {
    }

    /**
     * 修改用户状态
     */
    interface updateUserStatus {
    }

    /**
     * 用户分配角色
     */
    interface grantUserRole {
    }

    /**
     * 添加角色信息
     */
    interface addRole {
    }

    /**
     * 修改角色信息
     */
    interface updateRole {
    }

    /**
     * 修改角色状态
     */
    interface updateRoleStatus {
    }


}