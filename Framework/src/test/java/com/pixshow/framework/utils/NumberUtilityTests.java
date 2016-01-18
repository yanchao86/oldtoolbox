/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:NumberUtilityTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:May 13, 2013 6:28:14 PM
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
 * @since May 13, 2013
 * 
 */

public class NumberUtilityTests {
    public static void test_toString() {
        int radix = 62;
        for (long i = 0; i < Long.MAX_VALUE; i++) {
            String str = NumberUtility.toString(i, radix);
            long number = NumberUtility.parseLong(str, radix);
            System.out.println(i + " " + str + " " + number);
            if (number != i) {
                System.out.println("错误的:" + i + " " + str + " " + number);
                break;
            }
        }
    }

    public static void main(String[] args) {
        test_toString();
    }
}
