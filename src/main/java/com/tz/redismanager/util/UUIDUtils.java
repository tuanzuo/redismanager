package com.tz.redismanager.util;


import java.util.UUID;

public class UUIDUtils {

    public static String generateId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
