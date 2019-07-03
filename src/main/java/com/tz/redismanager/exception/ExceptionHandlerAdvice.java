package com.tz.redismanager.exception;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//https://docs.spring.io/spring-boot/docs/1.5.11.BUILD-SNAPSHOT/reference/htmlsingle/#boot-features-error-handling

@ControllerAdvice()
public class ExceptionHandlerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(HttpServletRequest request, Exception e) {
        this.getParams(request, e);
        return "exp";
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(RmException.class)
    @ResponseBody
    public Object handleRmException(HttpServletRequest request, Exception e) {
        this.getParams(request, e);
        return "exp";
    }

    private void getParams(HttpServletRequest request, Exception e) {
        ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
        //获取get请求的数据
        Map<String, String> reqParamMap = getParameterMap(request);
        String url = ((ContentCachingRequestWrapper) request).getRequestURI();
        String token = ((ContentCachingRequestWrapper) request).getHeader("token");
        //获取post请求的数据
        String reqBody = StringUtils.toEncodedString(wrapper.getContentAsByteArray(), Charset.forName(wrapper.getCharacterEncoding()));
        logger.info("[异常] {url:{},token:{},reqParam:{},reqBody:{},{}}", url, token, reqParamMap, reqBody, e.getMessage());
    }

    private static Map<String, String> getParameterMap(ServletRequest request) {
        // 返回值Map
        Map<String, String> returnMap = new HashMap();
        Iterator entries = request.getParameterMap().entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (String string : values) {
                    value = string + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }

}