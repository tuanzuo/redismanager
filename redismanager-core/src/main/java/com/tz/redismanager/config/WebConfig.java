package com.tz.redismanager.config;

import com.tz.redismanager.security.AuthInterceptor;
import com.tz.redismanager.security.AuthContextAttributeMethodProcessor;
import com.tz.redismanager.service.IUserStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * web配置
 *
 * @Since:2019-08-23 22:26:22
 * @Version:1.1.0
 */
@Configuration
public class WebConfig {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private IUserStatisticsService userStatisticsService;

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurerAdapter() {
            /**跨域设置-CROS解决跨域访问*/
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") //支持跨域访问的url，如果是所有url也可以设置为/**
                        .allowedOrigins("*")//允许跨域请求的网站，如果允许所有可以使用 * 【网站的链接不要使用/作为结尾，例如https://www.jd.com/】
                        //.allowedHeaders("content-type")
                        .allowedMethods("GET", "POST", "DELETE", "PUT")
                //.maxAge(3600);
                ;
            }
            //添加拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                //设置token验证的Interceptor v1.3.0
                registry.addInterceptor(new AuthInterceptor(stringRedisTemplate, userStatisticsService));
                super.addInterceptors(registry);
            }
            //添加参数解析器 v1.3.0
            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
                argumentResolvers.add(new AuthContextAttributeMethodProcessor());
                super.addArgumentResolvers(argumentResolvers);
            }
        };
    }
}
