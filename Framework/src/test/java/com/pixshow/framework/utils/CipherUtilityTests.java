/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:CipherUtilityTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Nov 21, 2013 1:49:30 PM
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
 * @since Nov 21, 2013
 * 
 */

public class CipherUtilityTests {
    public static void main(String[] args) {
        String password = "de4d381190964efc9708b79760bd35a3";
        String date = "http://weather.tv163.com/service/summary.do?latlng=39.943436,116.47409";
        String str = CipherUtility.AES.encrypt(date, password);
        System.out.println(str);
        str = CipherUtility.AES.decrypt(str, password);
        System.out.println(str);
    }
}
