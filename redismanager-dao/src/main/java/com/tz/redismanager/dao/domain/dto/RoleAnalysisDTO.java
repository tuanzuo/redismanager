package com.tz.redismanager.dao.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * <p>角色统计数据DTO</p>
 *
 * @version 1.5.0
 * @time 2020-10-17 16:07
 **/
@Getter
@Setter
@ToString
public class RoleAnalysisDTO {

    private Date createTime;

    private Integer roleCount;
}
