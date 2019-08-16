package com.tz.redismanager.validator;

/**
 * 验证分组接口
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


}