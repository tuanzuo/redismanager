package com.tz.redismanager.util;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * <p>日期工具类</p>
 *
 * @version 1.5.0
 * @time 2020-10-17 16:35
 **/
public class DateUtils {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
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

}
