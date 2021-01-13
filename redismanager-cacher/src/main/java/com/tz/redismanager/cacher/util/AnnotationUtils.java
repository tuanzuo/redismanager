package com.tz.redismanager.cacher.util;

import com.tz.redismanager.cacher.exception.CacherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * <p>注解工具类</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2021-01-13 23:49
 **/
public class AnnotationUtils {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationUtils.class);

    private static final String MEMBER_VALUES = "memberValues";

    /**
     * 获取注解的属性
     *
     * @param clazz
     * @param annotationType
     * @return
     */
    public static <A extends Annotation> Map<String, Object> getAnnotationAttributes(Class<?> clazz, Class<A> annotationType) {
        try {
            A annotation = org.springframework.core.annotation.AnnotationUtils.findAnnotation(clazz, annotationType);
            //Map<String, Object> memberValues = AnnotationUtils.getAnnotationAttributes(CacheableAspect.class.getAnnotation(Order.class));
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
            Field memberValuesField = invocationHandler.getClass().getDeclaredField(MEMBER_VALUES);
            ReflectionUtils.makeAccessible(memberValuesField);
            return (Map<String, Object>) memberValuesField.get(invocationHandler);
        } catch (NoSuchFieldException e) {
            throw new CacherException(e);
        } catch (IllegalAccessException e) {
            throw new CacherException(e);
        }
    }
}
