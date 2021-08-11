package com.tz.redismanager.service;

import com.tz.redismanager.domain.param.AnalysisParam;
import com.tz.redismanager.domain.vo.AnalysisRespVO;

/**
 * <p>Dashboard服务接口</p>
 *
 * @version 1.5.0
 * @time 2020-10-17 15:37
 **/
public interface IDashboardService {

    /**
     * 分析数据
     *
     * @param param
     * @return
     */
    AnalysisRespVO analysis(AnalysisParam param);

}
