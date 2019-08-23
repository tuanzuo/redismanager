package com.tz.redismanager.util;

import com.alibaba.fastjson.JSONArray;

/**
 * JSON工具类
 *
 * @Since:2019-08-23 22:52:32
 * @Version:1.1.0
 */
public class JsonUtils {

    public static String toJsonStr(Object object) {
        return JSONArray.toJSONString(object);
    }
}
