package com.tz.redismanager.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

//https://docs.spring.io/spring-boot/docs/1.5.11.BUILD-SNAPSHOT/reference/htmlsingle/#boot-features-error-handling

@ControllerAdvice()
public class ExceptionHandlerAdvice {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Object handleException(HttpServletRequest request, Exception e) throws Exception {
		return "exp";
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(RmException.class)
	@ResponseBody
	public Object handleRmException(HttpServletRequest request, Exception e) throws Exception {
		return "exp";
	}


}