package com.tz.redismanager.service;

import com.tz.redismanager.domain.dto.VisitDataDTO;
import com.tz.redismanager.domain.param.AnalysisParam;
import com.tz.redismanager.security.AuthContext;

/**
 * <p>统计接口</p>
 *
 * @version 1.4.0
 * @time 2020-10-12 22:35
 **/
public interface IStatisticService {

    void statisticsToAsync(AuthContext authContext);

    void addOnlineUser(Integer userId);

    void removeOnlineUser(Integer userId);

    Long countOnlineUser();

    void addVisit();

    VisitDataDTO countVisit(AnalysisParam param);
}
