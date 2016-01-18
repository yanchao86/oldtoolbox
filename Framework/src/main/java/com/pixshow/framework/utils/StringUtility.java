/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:StringUtility.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Mar 13, 2012 10:27:38 AM
 * 
 */
package com.pixshow.framework.utils;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.4 $ $Date: 2012/07/12 09:33:52 $
 * @since Mar 13, 2012
 * 
 */
public class StringUtility extends StringUtils {

    public static String toString(Object obj) {
        return obj == null ? null : obj.toString();
    }

    public static boolean isBoolean(String str) {
        if ("true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str)) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String... array) {
        for (String str : array) {
            if (isEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    public static String fillChar(String str, char add, int length, boolean asc) {
        if (str.length() >= length) {
            return str;
        }
        StringBuilder fill = new StringBuilder();
        if (!asc) {
            fill.append(str);
        }
        for (int i = 0; i < length - str.length(); i++) {
            fill.append(add);
        }
        return asc ? fill.toString() + str : fill.toString();
    }

}
