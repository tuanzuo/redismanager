package com.tz.redismanager.test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * <p></p>
 *
 * @version 1.0
 * @time 2020-08-14 8:13
 **/
public class FunctionFactory {

    private Map<String, Function<String,String>> map = new HashMap<>();

    public BiConsumer<Object, Function<Object,Object>> operateConsumer() {
        return null;
    }
}
