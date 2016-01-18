/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:NumberUtility.java Project: LvFramework
 * 
 * Creator:4399-lvtu-8 
 * Date:Jan 17, 2013 3:29:53 PM
 * 
 */
package com.pixshow.framework.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Jan 17, 2013
 * 
 */

public class NumberUtility {

    private final static char[] digits = {
                                       //
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',// 
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', //
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',//
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',//
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' //
                                       };

    public static double scale(String num, int max) {
        if (num == null || "".equals(num))
            return 0;
        BigDecimal bd = new BigDecimal(num.trim());
        bd = bd.setScale(max, RoundingMode.DOWN);
        return bd.doubleValue();
    }

    public static List<Long> intToLong(List<Integer> intList) {
        List<Long> longList = new ArrayList<Long>(intList.size());
        for (Integer i : intList) {
            longList.add(i.longValue());
        }
        return longList;
    }

    public static List<Integer> longToInt(List<Long> longList) {
        List<Integer> intList = new ArrayList<Integer>(longList.size());
        for (Long i : longList) {
            intList.add(i.intValue());
        }
        return intList;
    }

    public static List<Integer> stringToInt(List<String> stringList) {
        List<Integer> intList = new ArrayList<Integer>(stringList.size());
        for (String str : stringList) {
            intList.add(NumberUtils.toInt(str));
        }

        return intList;
    }

    public static String splitPoint(Object num, int length, int splitPoint, boolean asc) {
        String numStr = num.toString();
        if (numStr.length() < splitPoint) {
            return numStr;
        }
        if (!asc) {
            numStr = numStr.substring(splitPoint);
        } else {
            numStr = numStr.substring(0, numStr.length() - splitPoint);
        }
        numStr = StringUtility.fillChar(numStr, '0', length, false);
        return numStr;
    }

    public static String toString(long i, int radix) {
        if (radix == 10)
            return Long.toString(i);
        char[] buf = new char[65];
        int charPos = 64;
        boolean negative = (i < 0);
        if (!negative) {
            i = -i;
        }
        while (i <= -radix) {
            buf[charPos--] = digits[(int) (-(i % radix))];
            i = i / radix;
        }
        buf[charPos] = digits[(int) (-i)];
        if (negative) {
            buf[--charPos] = '-';
        }
        return new String(buf, charPos, (65 - charPos));
    }

    private static int digit(char ch) {
        if ('0' <= ch && ch <= '9') {
            return ch - '0';
        } else if ('a' <= ch && ch <= 'z') {
            return 9 + 1 + (ch - 'a');
        } else if ('A' <= ch && ch <= 'Z') {
            return 9 + 26 + 1 + (ch - 'A');
        }
        return '0';
    }

    public static long parseLong(String s, int radix) throws NumberFormatException {
        char[] arr = s.toCharArray();
        long cardinal = 1;
        long result = 0;
        for (int i = arr.length - 1; i >= 0; i--) {
            char c = arr[i];
            if ('-' == c) {
                result = -result;
            } else {
                int digit = digit(c);
                result += (cardinal * digit);
            }
            cardinal *= radix;
        }
        return result;
    }
    
    public static boolean isNumber(String str){
        return NumberUtils.isNumber(str);
    }
}
