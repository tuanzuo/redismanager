package com.tz.redismanager.util;

import java.util.UUID;

/**
 * UUID工具类
 *
 * @Since:2019-08-23 22:54:55
 * @Version:1.1.0
 */
public class UUIDUtils {

    public static String generateId() {
        return randomUUID().replaceAll("-", "");
    }

    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

}
