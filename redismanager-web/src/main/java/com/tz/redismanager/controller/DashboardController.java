package com.tz.redismanager.controller;

import com.tz.redismanager.annotation.MethodLog;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.AnalysisParam;
import com.tz.redismanager.domain.vo.AnalysisRespVO;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.limiter.annotation.Limiter;
import com.tz.redismanager.security.annotation.Auth;
import com.tz.redismanager.service.IDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仪表盘管理
 *
 * @author tuanzuo
 * @version 1.5.0
 * @time 2020-10-17 15:29
 **/
@RestController
@RequestMapping("/auth/dashboard")
public class DashboardController {

    @Autowired
    private IDashboardService dashboardService;

    /**
     * 查询分析数据接口
     * @param param
     * @return
     */
    @RequestMapping("analysis")
    @Auth
    @MethodLog(logPrefix = "查询分析数据", logInputParams = false, logOutputParams = false)
    @Limiter(name = "查询分析数据请求限流", key = "DASHBOARD_ANALYSIS_API", qps = 200)
    public ApiResult<AnalysisRespVO> analysis(@RequestBody @Validated AnalysisParam param) {
        return new ApiResult<>(ResultCode.SUCCESS, dashboardService.analysis(param));
    }

}
