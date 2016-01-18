/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:DiTuUtilityTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:May 17, 2013 3:01:02 PM
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
 * @since May 17, 2013
 * 
 */

public class DiTuUtilityTests {
    public static void main(String[] args) {
        System.out.println(DiTuUtility.getAddressFromGoogoleMap(33.28462, 55.722656, "en"));
        System.out.println(DiTuUtility.getAddressFromGoogoleMap(22.917923, -100.722656, "zh_cn"));
    }
}
