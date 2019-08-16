package com.tz.redismanager.exception;

import com.tz.redismanager.bean.ApiResult;
import com.tz.redismanager.bean.ResultCode;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.util.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//https://docs.spring.io/spring-boot/docs/1.5.11.BUILD-SNAPSHOT/reference/htmlsingle/#boot-features-error-handling

@ControllerAdvice()
public class ExceptionHandlerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(HttpServletRequest request, Exception e) {
        this.getParams(request, e);
        return new ApiResult<>(ResultCode.FAIL.getCode(), CommonUtils.getExcpMsg(e));
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(RmException.class)
    @ResponseBody
    public Object handleRmException(HttpServletRequest request, Exception e) {
        this.getParams(request, e);
        return new ApiResult<>(ResultCode.FAIL.getCode(), CommonUtils.getExcpMsg(e));
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseBody
    public Object handleBindException(HttpServletRequest request, Exception e) {
        this.getParams(request, e);
        StringBuilder msgBuilder = new StringBuilder();
        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException exception = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
            for (ConstraintViolation<?> item : violations) {
                msgBuilder.append(ConstInterface.Symbol.COMMA).append(item.getMessage());
            }
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
            BindingResult bindingResult = exception.getBindingResult();
            bindingResult.getAllErrors().forEach(temp -> {
                if (null != temp) {
                    msgBuilder.append(ConstInterface.Symbol.COMMA);
                    if (temp instanceof FieldError) {
                        FieldError fieldError = (FieldError) temp;
                        msgBuilder.append(fieldError.getField()).append(ConstInterface.Symbol.COLON).append(fieldError.getDefaultMessage());
                    } else if (temp instanceof ObjectError) {
                        msgBuilder.append(temp.getDefaultMessage());
                    }
                }
            });
        }
        String msg = msgBuilder.toString();
        if (StringUtils.isNotBlank(msg)) {
            msg = msg.substring(1);
        } else {
            msg = CommonUtils.getExcpMsg(e);
        }
        return new ApiResult<>(ResultCode.FAIL.getCode(), msg);
    }

    private void getParams(HttpServletRequest request, Exception e) {
        ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
        //获取get请求的数据
        Map<String, String> reqParamMap = getParameterMap(request);
        String url = wrapper.getRequestURI();
        String token = wrapper.getHeader("token");
        //获取post请求的数据
        String reqBody = StringUtils.toEncodedString(wrapper.getContentAsByteArray(), Charset.forName(wrapper.getCharacterEncoding()));
        logger.error("[异常] {url:{},token:{},reqParam:{},reqBody:{}}", url, token, reqParamMap, reqBody, e);
    }

    private static Map<String, String> getParameterMap(ServletRequest request) {
        // 返回值Map
        Map<String, String> returnMap = new HashMap<>();
        request.getParameterMap().forEach((key, temp) -> {
            String name = key;
            String value = "";
            Object valueObj = temp;
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (String string : values) {
                    value = string + ConstInterface.Symbol.COMMA;
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        });
        return returnMap;
    }

}