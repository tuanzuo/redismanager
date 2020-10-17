package com.tz.redismanager.controller;

import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.security.Auth;
import com.tz.redismanager.service.IDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p></p>
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
    public ApiResult<?> analysis() {
        return dashboardService.analysis();
    }

}
