package com.tz.redismanager.service;

/**
 * <p>缓存初始化回调接口</p>
 *
 * @version 1.5.0
 * @time 2020-10-18 10:56
 **/
public interface ICacheInitCallbackService {

    /**
     * 缓存初始化
     *
     * @param param
     * @return
     */
    Object init(Object param);
}
