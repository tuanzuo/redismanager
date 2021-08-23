package com.tz.redismanager.util;

import com.alibaba.fastjson.JSONArray;

import java.lang.reflect.Type;

/**
 * JSON工具类
 *
 * @author tuanzuo
 * @Since:2019-08-23 22:52:32
 * @Version:1.1.0
 */
public class JsonUtils {

    public static String toJsonStr(Object object) {
        return JSONArray.toJSONString(object);
    }

    /**
     * json转为对象
     * 例如1：List<VO> list = JSON.parseObject("...", new TypeReference<List<VO>>() {});
     * 例如2：
     * final static Type type = new TypeReference<List<VO>>() {}.getType();
     * List<VO> list = JSON.parseObject("...", type);
     * https://github.com/alibaba/fastjson/wiki/TypeReference
     * @param <T>
     * @param text
     * @param type
     * @return
     */
    public static <T> T parseObject(String text, Type type) {
        return JSONArray.parseObject(text, type);
    }
}
