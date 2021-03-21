package com.tz.redismanager.domain.param;

import com.tz.redismanager.enm.DateTypeEnum;
import com.tz.redismanager.validator.DateTypeNotNull;
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
@DateTypeNotNull(message = "日期类型不能为空或者非DateTypeEnum中定义的数据")
public class AnalysisParam {

    /**
     * 日期类型
     * {@link com.tz.redismanager.enm.DateTypeEnum}
     */
    private String dateType = DateTypeEnum.YEAR.getType();

}
