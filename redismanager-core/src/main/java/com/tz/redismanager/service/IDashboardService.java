package com.tz.redismanager.service;

import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.AnalysisParam;

/**
 * <p>Dashboard服务接口</p>
 *
 * @version 1.5.0
 * @time 2020-10-17 15:37
 **/
public interface IDashboardService {

    ApiResult<?> analysis(AnalysisParam param);

}
