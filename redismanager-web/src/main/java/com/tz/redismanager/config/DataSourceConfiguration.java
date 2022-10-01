package com.tz.redismanager.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 数据库配置
 *
 * @author tuanzuo
 * @version 1.7.2
 * @time 2022-10-01 11:34
 **/
@Configuration
public class DataSourceConfiguration {

    /**
     * 自定义DruidDataSource
     * @see org.springframework.boot.autoconfigure.jdbc.DataSourceConfiguration.Generic 默认构建一个通用的DataSource(不能设置maxActive等参数)
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource buildDruidDataSource() {
        return new DruidDataSource();
    }
}
