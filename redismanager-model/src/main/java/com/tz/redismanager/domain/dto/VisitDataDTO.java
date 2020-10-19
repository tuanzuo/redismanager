package com.tz.redismanager.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>访问数据DTO</p>
 *
 * @version 1.5.0
 * @time 2020-10-17 18:02
 **/
@Getter
@Setter
public class VisitDataDTO {
    private Long total;
    private Long currentYearTotal;
    private Long currentMonthTotal;
    private Long currentDayTotal;
    private List<totalDeail> totalDeails = new ArrayList<>();
    private List<totalDeail> yearDeails = new ArrayList<>();
    private List<totalDeail> monthDeails = new ArrayList<>();
    private List<totalDeail> weekDeails = new ArrayList<>();
    private List<totalDeail> dayDeails = new ArrayList<>();
    private List<totalDeail> currentQueryDetails = new ArrayList<>();

    public void addTotalDeails(totalDeail deail){
        totalDeails.add(deail);
    }

    public void addYearDeails(totalDeail deail){
        yearDeails.add(deail);
    }

    public void addMonthDeails(totalDeail deail){
        monthDeails.add(deail);
    }

    public void addWeekDeails(totalDeail deail){
        weekDeails.add(deail);
    }

    public void addDayDeails(totalDeail deail){
        dayDeails.add(deail);
    }

    @Getter
    @Setter
    public static class totalDeail{
        private String date;
        private Long count;
    }
}
