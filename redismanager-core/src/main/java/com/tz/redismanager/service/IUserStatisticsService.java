package com.tz.redismanager.service;

/**
 * <p>用户统计接口</p>
 *
 * @version 1.4.0
 * @time 2020-10-12 22:35
 **/
public interface IUserStatisticsService {

    void addOnlineUser(Integer userId);

    void removeOnlineUser(Integer userId);

    Long countOnlineUser();
}
