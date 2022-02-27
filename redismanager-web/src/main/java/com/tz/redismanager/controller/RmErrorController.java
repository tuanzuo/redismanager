package com.tz.redismanager.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 错误页面管理
 *
 * @author tuanzuo
 * @version 1.0
 * @time 2019-07-02 20:50
 **/
@Controller
public class RmErrorController implements ErrorController {

    /**getErrorPath()废弃了，需要通过server.error.path来设置*/
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public String toIndex() {
        return "/";
    }
}
