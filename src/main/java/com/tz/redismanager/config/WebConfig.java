package com.tz.redismanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * web配置
 *
 * @Since:2019-08-23 22:26:22
 * @Version:1.1.0
 */
@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        /**跨域设置*/
        return new WebMvcConfigurerAdapter() {
            /**CROS解决跨域访问*/
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") //支持跨域访问的url，如果是所有url也可以设置为/**
                        .allowedOrigins("*")//允许跨域请求的网站，如果允许所有可以使用 * 【网站的链接不要使用/作为结尾，例如https://www.jd.com/】
                        //.allowedHeaders("content-type")
                        .allowedMethods("GET", "POST", "DELETE", "PUT")
                //.maxAge(3600);
                ;
            }
        };
    }
}
