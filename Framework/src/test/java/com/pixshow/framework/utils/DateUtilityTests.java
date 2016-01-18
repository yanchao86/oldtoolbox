/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:DateUtilityTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:May 23, 2013 10:52:20 AM
 * 
 */
package com.pixshow.framework.utils;

import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since May 23, 2013
 * 
 */

public class DateUtilityTests {
    public static void main(String[] args) {
        for (int i = 0; i <= 24; i++) {
            System.out.println(("GMT-" + i + "   ") + DateUtility.format(new Date(), "yyyy-MM-dd", TimeZone.getTimeZone("GMT-" + i)));
        }

        for (int i = 0; i <= 24; i++) {
            System.out.println(("GMT+" + i + "   ") + DateUtility.format(new Date(), "yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT+" + i)));
        }

        System.out.println(Arrays.toString(TimeZone.getAvailableIDs()));
        String utc = "+ 03";
        System.out.println(utc + " " + DateUtility.format(new Date(), "yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone(utc)));
    }
}
