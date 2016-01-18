/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:DigestUtilityTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Nov 21, 2013 2:05:33 PM
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

public class DigestUtilityTests {
    public static void main(String[] args) {
        String url = "http://developer.baidu.com/wiki/index.php?title=docs/cplat/push/api";
        for (int i = 0; i < 10; i++) {
            String newUrl = url + (char) ('a' + i);
            System.out.println(newUrl);
            System.out.println(Base64Utility.encodeBase64URLSafeString(DigestUtility.md5(newUrl)));
            System.out.println(DigestUtility.md5Hex(newUrl));
        }

    }
}
