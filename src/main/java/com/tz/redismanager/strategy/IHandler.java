package com.tz.redismanager.strategy;

/**
 * 处理器接口
 *
 * @version 1.0
 * @time 2019-06-23 21:17
 **/
public interface IHandler<T, R> {

    public R handle(T t);
}
