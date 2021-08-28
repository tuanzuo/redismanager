package com.tz.redismanager.service;

import com.tz.redismanager.domain.dto.RedisConfigVisitDataDTO;
import com.tz.redismanager.domain.dto.UserVisitDataDTO;
import com.tz.redismanager.domain.dto.VisitDataDTO;
import com.tz.redismanager.domain.param.AnalysisParam;
import com.tz.redismanager.security.domain.AuthContext;

/**
 * <p>统计接口</p>
 *
 * @author tuanzuo
 * @version 1.4.0
 * @time 2020-10-12 22:35
 **/
public interface IStatisticService {

    /**
     * 异步统计
     *
     * @param authContext
     */
    void statisticsToAsync(AuthContext authContext);

    /**
     * 增加在线人数
     *
     * @param userId
     */
    void addOnlineUser(Long userId);

    /**
     * 删除在线人数
     *
     * @param userId
     */
    void removeOnlineUser(Long userId);

    /**
     * 统计在线人数
     *
     * @return
     */
    Long countOnlineUser();

    /**
     * 增加访问数
     */
    void addVisit();

    /**
     * 统计访问数
     *
     * @param param
     * @return
     */
    VisitDataDTO countVisit(AnalysisParam param);

    /**
     * 统计用户访问量
     *
     * @param param
     * @return
     */
    UserVisitDataDTO countUserVisit(AnalysisParam param);

    /**
     * 增加redis连接访问量
     *
     * @param redisConfigId
     */
    void addRedisConfigVisit(Long redisConfigId);

    /**
     * 统计redis连接访问量
     *
     * @param param
     * @return
     */
    RedisConfigVisitDataDTO countRedisConfigVisit(AnalysisParam param);
}
