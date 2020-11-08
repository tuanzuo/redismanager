package com.tz.redismanager.util;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;

/**
 * <p>日期工具类</p>
 *
 * @version 1.5.0
 * @time 2020-10-17 16:35
 **/
public class DateUtils {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD_HH = "yyyy-MM-dd HH";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String YYYYMMDDHH = "yyyyMMddHH";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYMM = "yyyyMM";
    public static final String YYYY = "yyyy";
    public static final String MM = "MM";
    public static final String DD = "dd";
    public static final String HH = "HH";

    public static String dateToStr(Date date, String pattern) {
        return new DateTime(date.getTime()).toString(pattern);
    }

    public static String nowDateToStr(String pattern) {
        return new DateTime().toString(pattern);
    }

    public static Date strToDate(String date, String pattern) {
        return DateTime.parse(date, DateTimeFormat.forPattern(pattern)).toDate();
    }

    public static CurrentDate getCurrentDate() {
        return new CurrentDate();
    }

    public static Date addHours(Date date, int hours) {
        return new DateTime(date.getTime()).plusHours(hours).toDate();
    }

    @Getter
    @Setter
    public static class CurrentDate {
        String YYYY = DateUtils.nowDateToStr(DateUtils.YYYY);
        String YYYYMM = DateUtils.nowDateToStr(DateUtils.YYYYMM);
        String YYYYMMDD = DateUtils.nowDateToStr(DateUtils.YYYYMMDD);
        String YYYYMMDDHH = DateUtils.nowDateToStr(DateUtils.YYYYMMDDHH);
        String MM = DateUtils.nowDateToStr(DateUtils.MM);
        String DD = DateUtils.nowDateToStr(DateUtils.DD);
        String HH = DateUtils.nowDateToStr(DateUtils.HH);
        String YYYY_MM = DateUtils.nowDateToStr(DateUtils.YYYY_MM);
        String YYYY_MM_DD = DateUtils.nowDateToStr(DateUtils.YYYY_MM_DD);
    }

}
