package com.tz.redismanager.dao.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <p>用户统计数据DTO</p>
 *
 * @version 1.5.0
 * @time 2020-10-17 16:07
 **/
@Getter
@Setter
public class UserAnalysisDTO {

    private Date registerDate;
    private Integer userCount;
}
