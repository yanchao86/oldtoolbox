/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:SunriseUtility.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Apr 8, 2013 8:48:55 PM
 * 
 */
package com.pixshow.framework.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Apr 8, 2013
 * 
 */

public class AstronomyUtility {
    public static Date getSunrise(double lat, double lng) {
        return getSunrise(lat, lng, new Date());
    }

    public static Date getSunrise(double lat, double lng, Date date) {
        Location location = new Location(Double.toString(lat), Double.toString(lng));
        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, TimeZone.getDefault());
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Calendar officialSunrise = calculator.getOfficialSunriseCalendarForDate(c);
        return officialSunrise.getTime();
    }

    public static Date getSunset(double lat, double lng) {
        return getSunset(lat, lng, new Date());
    }

    public static Date getSunset(double lat, double lng, Date date) {
        Location location = new Location(Double.toString(lat), Double.toString(lng));
        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, TimeZone.getDefault());
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Calendar officialSunset = calculator.getOfficialSunsetCalendarForDate(c);
        return officialSunset.getTime();
    }

    public static boolean isNight(double lat, double lng) {
        return !isDay(lat, lng);
    }

    public static boolean isNight(double lat, double lng, Date curDate) {
        return !isDay(lat, lng, curDate);
    }

    public static boolean isDay(double lat, double lng) {
        return isDay(lat, lng, new Date());
    }

    public static boolean isDay(double lat, double lng, Date curDate) {
        Date sunrise = getSunrise(lat, lng, curDate);
        Date sunset = getSunset(lat, lng, curDate);
        return sunrise.getTime() <= curDate.getTime() && curDate.getTime() <= sunset.getTime() ? true : false;
    }
}
