/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:UUIDUtilityTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Mar 13, 2013 12:12:44 PM
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
 * @since Mar 13, 2013
 * 
 */

public class UUIDUtilityTests {
    public static void main(String[] args) {
        System.out.println(UUIDUtility.uuid22());
        System.out.println(UUIDUtility.uuid32());
        System.out.println(UUIDUtility.uuid36());
        System.out.println("-------------------------------------------");
        //2fff3f4a-b7fa-423e-adf3-d5c12d6b846e
        //L_8_Srf6Qj6t89XBLWuEbg
        String uuid36 = "b0afc088-3cf7-424d-afcc-a3067138acf2";
        System.out.println(uuid36);
        String uuid22 = UUIDUtility.uuid22f36(uuid36);
        System.out.println(uuid22);
        String uuid36New = UUIDUtility.uuid36f22(uuid22);
        System.out.println(uuid36.equals(uuid36New) + " -> " + uuid36New);

    }
}
