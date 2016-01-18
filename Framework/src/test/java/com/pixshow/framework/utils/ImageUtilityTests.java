/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:ImageUtilityTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Dec 11, 2013 11:38:34 AM
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
 * @since Dec 11, 2013
 * 
 */

public class ImageUtilityTests {

    public static void test_getImageInfo() {
        String fileName = "F:/图片/wallpaper/原始/4aeebefc-ced0-4b1c-9d02-fbc8ba35d882.jpg";
        long time1 = System.currentTimeMillis();
        //        System.out.println(ImageUtility.getImageInfo(fileName));
        System.out.println(ImageUtility.getImageInfo4GM(fileName));
        long time2 = System.currentTimeMillis();
        System.out.println((time2 - time1));
    }

    public static void main(String[] args) {
        test_getImageInfo();
    }
}
