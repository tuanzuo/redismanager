package com.tz.redismanager.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>用户访问数据DTO</p>
 *
 * @version 1.5.0
 * @time 2020-10-17 18:02
 **/
@Getter
@Setter
public class UserVisitDataDTO {
    private Long currentYearTotal;
    private Long currentMonthTotal;
    private Long currentDayTotal;
    private List<Detail> yearDeails = new ArrayList<>();
    private List<Detail> monthDeails = new ArrayList<>();
    private List<Detail> weekDeails = new ArrayList<>();
    private List<Detail> dayDeails = new ArrayList<>();
    private List<Detail> currentQueryDetails = new ArrayList<>();
    private List<Detail> rangeDetails = new ArrayList<>();

    public void addYearDeails(Detail deail){
        yearDeails.add(deail);
    }

    public void addMonthDeails(Detail deail){
        monthDeails.add(deail);
    }

    public void addWeekDeails(Detail deail){
        weekDeails.add(deail);
    }

    public void addDayDeails(Detail deail){
        dayDeails.add(deail);
    }

    public void addRangeDeails(Detail deail){
        rangeDetails.add(deail);
    }

    @Getter
    @Setter
    public static class Detail {
        private String date;
        //Long/Double
        private Object count;
    }
}
