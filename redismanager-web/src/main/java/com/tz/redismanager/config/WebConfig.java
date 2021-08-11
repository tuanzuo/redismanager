package com.tz.redismanager.config;

import com.tz.redismanager.security.auth.AuthContextAttributeMethodProcessor;
import com.tz.redismanager.security.auth.AuthInterceptor;
import com.tz.redismanager.service.IStatisticService;
import com.tz.redismanager.token.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
    private ITokenService tokenAuthService;
    @Autowired
    private IStatisticService statisticService;

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        /**
         * {@link WebMvcConfigurerAdapter} 已经废弃了，直接使用WebMvcConfigurer v1.5.0
         */
        return new WebMvcConfigurer() {
            /**跨域设置-CROS解决跨域访问*/
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        //支持跨域访问的url，如果是所有url也可以设置为/**
                        .addMapping("/**")
                        //允许跨域请求的网站，如果允许所有可以使用*【网站的链接不要使用/作为结尾，例如https://www.jd.com/】
                        .allowedOrigins("*")
                        .allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE")
                        .allowedHeaders("*")
                        //是否允许发送Cookie.默认情况下，不发送Cookie，即：false。对服务器有特殊要求的请求，比如请求方法是PUT或DELETE，或者Content-Type字段的类型是application/json，这个值只能设为true。如果服务器不要浏览器发送Cookie，删除该字段即可。
                        .allowCredentials(true)
                        //用来指定本次预检请求的有效期，单位为秒。在有效期间，不用发出另一条预检请求。如果在开发中，发现每次发起请求都是两条，一次OPTIONS，一次正常请求，注意是每次，那么就需要配置Access-Control-Max-Age，避免每次都发出预检请求。
                        .maxAge(3600);
            }
            //添加拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                //设置token验证的Interceptor v1.3.0
                registry.addInterceptor(new AuthInterceptor(tokenAuthService, statisticService));
                //super.addInterceptors(registry);
            }
            //添加参数解析器 v1.3.0
            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
                argumentResolvers.add(new AuthContextAttributeMethodProcessor());
                //super.addArgumentResolvers(argumentResolvers);
            }
        };
    }

}
