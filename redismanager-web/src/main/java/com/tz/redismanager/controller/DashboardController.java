package com.tz.redismanager.controller;

import com.tz.redismanager.annotation.MethodLog;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.AnalysisParam;
import com.tz.redismanager.security.domain.Auth;
import com.tz.redismanager.service.IDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>仪表盘controller</p>
 *
 * @version 1.5.0
 * @time 2020-10-17 15:29
 **/
@RestController
@RequestMapping("/auth/dashboard")
public class DashboardController {

    @Autowired
    private IDashboardService dashboardService;

    @RequestMapping("analysis")
    @Auth
    @MethodLog(logPrefix = "查询分析数据", logInputParams = false, logOutputParams = false)
    public ApiResult<?> analysis(@RequestBody AnalysisParam param) {
        return dashboardService.analysis(param);
    }

}
