package com.tz.redismanager.domain.param;

import com.tz.redismanager.enm.DateTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>分析数据参数</p>
 *
 * @version 1.5.0
 * @time 2020-10-17 23:06
 **/
@Getter
@Setter
public class AnalysisParam {

    /**
     * 日期类型
     * {@link com.tz.redismanager.enm.DateTypeEnum}
     */
    private String dateType = DateTypeEnum.YEAR.getType();

}
