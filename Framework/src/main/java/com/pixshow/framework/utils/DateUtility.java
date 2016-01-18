/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:DateUtility.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Mar 13, 2012 10:27:38 AM
 * 
 */
package com.pixshow.framework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.1 $ $Date: 2012/03/13 02:27:03 $
 * @since Mar 13, 2012
 * 
 */
public class DateUtility extends DateUtils {
    private static final String   DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final TimeZone timeZone             = TimeZone.getTimeZone("GMT+8");

    public static String format(Date date) {
        return format(date, DEFAULT_DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {

        return date == null ? null : DateFormatUtils.format(date, pattern);
    }

    public static String format(Calendar calendar) {
        return format(calendar, DEFAULT_DATE_PATTERN);
    }

    public static String format(Calendar calendar, String pattern) {
        return calendar == null ? null : DateFormatUtils.format(calendar, pattern);
    }

    public static String format(Calendar calendar, String pattern, TimeZone timeZone) {
        return DateFormatUtils.format(calendar, pattern, timeZone);
    }

    public static String format(Date date, String pattern, TimeZone timeZone) {
        return DateFormatUtils.format(date, pattern, timeZone);
    }

    public static String format4UnixTime(int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date * 1000L);
        return format(calendar, DEFAULT_DATE_PATTERN);
    }

    public static String format(int date, String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date * 1000L);
        return format(calendar, pattern);
    }

    public static Date parseDate(String str, Locale locale, String parsePattern) {
        if (StringUtils.isBlank(str)) { return null; }
        SimpleDateFormat format = new SimpleDateFormat(parsePattern, locale);
        try {
            return format.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseDate(int curr) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(curr * 1000L);
        return c.getTime();
    }

    public static Date parseDate(String str, String parsePatter) {
        SimpleDateFormat format = new SimpleDateFormat(parsePatter);
        try {
            return format.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    public static int currentUnixTime() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * 
     * 
     * @deprecated
     * @see #parseUnixTime(String)
     * @param dateStr
     * @author 颜超
     * @return
     */
    public static int currentUnixTime(String dateStr) {
        return parseUnixTime(dateStr);
    }

    /**
     * 
     * @deprecated
     * @see #parseUnixTime(String)
     * @param dateStr
     * @author 颜超
     * @return
     */
    public static Integer currentUnixTime(Date date) {
        return parseUnixTime(date);
    }

    public static int parseUnixTime(String dateStr, String parsePatter) {
        Date date = parseDate(dateStr, parsePatter);
        return (int) (date.getTime() / 1000);
    }

    public static int parseUnixTime(String dateStr) {
        return parseUnixTime(dateStr, DEFAULT_DATE_PATTERN);
    }

    public static Integer parseUnixTime(Date date) {
        if (date == null) { return null; }
        return (int) (date.getTime() / 1000);
    }

    /**
     * 根据时区计算这个时间属于哪一天
     * 
     * @param localTime
     *            本地当天24点的时间戳
     * @param paramTime
     *            要计算的时间戳
     * @return
     * 
     */
    public static int timezone(int localTime, int paramTime) {
        int day = 24 * 60 * 60;
        int result = localTime;
        if (paramTime > 0) {
            result = (localTime - paramTime) % day + paramTime - day;
        }
        return result;
    }

    /**
     * 判断时间是否属于今天
     * 
     * @param date
     * @return
     * 
     */
    public static boolean isToday(int date) {
        Calendar c = Calendar.getInstance();
        int tY = c.get(Calendar.YEAR);
        int tM = c.get(Calendar.MONTH);
        int tD = c.get(Calendar.DAY_OF_MONTH);

        c.setTimeInMillis(date * 1000L);
        int Y = c.get(Calendar.YEAR);
        int M = c.get(Calendar.MONTH);
        int D = c.get(Calendar.DAY_OF_MONTH);

        return tY == Y && tM == M && tD == D;
    }

    public static int getTodayToUnixTime() {
        return (int) (getToday().getTime() / 1000);
    }

    public static Date getToday() {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(timeZone);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return c.getTime();
    }

    public static String getCron(String date) {
        Date d = parseDate(date, DEFAULT_DATE_PATTERN);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c.get(Calendar.SECOND) + " " + c.get(Calendar.MINUTE) + " " + c.get(Calendar.HOUR_OF_DAY) + " " + c.get(Calendar.DAY_OF_MONTH) + " " + (c.get(Calendar.MONTH) + 1) + " ? " + c.get(Calendar.YEAR);
    }

    public static int getDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DAY_OF_WEEK) - 1;
        return day != 0 ? day : 7;
    }
}
