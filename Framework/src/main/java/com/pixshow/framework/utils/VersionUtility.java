/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:VersionUtility.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Dec 3, 2013 1:33:12 PM
 * 
 */
package com.pixshow.framework.utils;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$
 * @since Dec 3, 2013
 * 
 */

public class VersionUtility {
    public static boolean afterVersion(String version1, String version2) {
        if (StringUtility.isEmpty(version1)) return false;
        if (StringUtility.isEmpty(version2)) return false;
        return version(version2) - version(version1) >= 0 ? true : false;
    }

    public static boolean beforeVersion(String version1, String version2) {
        return !afterVersion(version1, version2);
    }

    public static int version(String str) {
        str = StringUtility.isEmpty(str) ? "" : str;
        String[] vers = StringUtils.split(str, ".");
        //000,000,000
        int version = 0;
        for (int i = 0; i < 3; i++) {
            if (i >= vers.length) {
                break;
            }
            if (i == 0) {
                version += toInt(vers[i]) * 1000000;
            } else if (i == 1) {
                version += toInt(vers[i]) * 1000;
            } else if (i == 2) {
                version += toInt(vers[i]);
            }
        }
        return version;
    }

    public static String version(int ver) {
        int v1 = ver / 1000000;
        int v2 = (ver - v1 * 1000000) / 1000;
        int v3 = (ver - v1 * 1000000 - v2 * 1000);
        return v1 + "." + v2 + "." + v3;
    }

    /**
     * android123 -> 123 123 -> 123 v123 -> 123
     * 
     * @param str
     * @return
     * 
     */
    private static int toInt(String str) {
        char[] chars = str.toCharArray();
        StringBuilder number = new StringBuilder();
        boolean begin = false;
        boolean end = false;
        for (char c : chars) {
            if (end) {
                break;
            }
            if (isNumber(c)) {
                begin = true;
                number.append(c);
            } else {
                end = begin ? true : false;
            }
        }
        return number.length() > 0 ? Integer.valueOf(number.toString()) : 0;
    }

    private static boolean isNumber(char c) {
        return c >= '0' && c <= '9' ? true : false;
    }

    public static void main(String[] args) {
        int vInt = version("2.2.2");
        System.out.println(vInt);
        System.out.println(version(vInt));

        vInt = version("0.3.2");
        System.out.println(vInt);
        System.out.println(version(vInt));

        vInt = version("0.0.212");
        System.out.println(vInt);
        System.out.println(version(vInt));

        vInt = version("10.123.212");
        System.out.println(vInt);
        System.out.println(version(vInt));

        System.out.println(afterVersion("2.6", "2.5.5"));
    }
}
