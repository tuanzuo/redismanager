package com.tz.redismanager.util;

import com.alibaba.fastjson.JSONArray;

public class JsonUtils {

    public static String toJsonStr(Object object){
       return JSONArray.toJSONString(object);
    }
}
