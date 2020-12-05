package com.tz.redismanager.domain.param;

import com.tz.redismanager.enm.DateTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnalysisParam that = (AnalysisParam) o;
        return Objects.equals(dateType, that.dateType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateType);
    }
}
