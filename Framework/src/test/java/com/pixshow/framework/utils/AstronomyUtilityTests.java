/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:AstronomyUtilityTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Apr 9, 2013 11:01:15 AM
 * 
 */
package com.pixshow.framework.utils;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Apr 9, 2013
 * 
 */

public class AstronomyUtilityTests {
    public static void main(String[] args) {
        double[] latlng = { 39.980277, 116.325989 };
        System.out.println("日出" + DateUtility.format(AstronomyUtility.getSunrise(latlng[0], latlng[1])));
        System.out.println("日落" + DateUtility.format(AstronomyUtility.getSunset(latlng[0], latlng[1])));
        System.out.println(AstronomyUtility.isDay(latlng[0], latlng[1]));
        System.out.println(AstronomyUtility.isNight(latlng[0], latlng[1]));
    }
}
