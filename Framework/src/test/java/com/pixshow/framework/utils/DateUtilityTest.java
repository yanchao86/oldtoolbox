/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:DateUtilityTest.java Project: LvFramework
 * 
 * Creator:4399-lvtu-8 
 * Date:May 24, 2013 3:30:07 PM
 * 
 */
package com.pixshow.framework.utils;

import java.util.Calendar;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateUtils;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since May 24, 2013
 * 
 */

public class DateUtilityTest extends DateUtils {

    private static final String   DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final TimeZone timeZone             = TimeZone.getTimeZone("GMT+8");
    
    
    public static void main(String[] args) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT"+(-1)));
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        System.out.println(c.get(Calendar.DAY_OF_WEEK)+"__"+c.getTimeInMillis());
        
        c.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        System.out.println(c.get(Calendar.DAY_OF_WEEK)+"__"+c.getTimeInMillis());
    }

}
