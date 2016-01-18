/*
 * Copyright (c) 2010-2012 www.pixshow.net All Rights Reserved
 *
 * File:Base64Utility.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Nov 8, 2012 5:55:52 PM
 * 
 */
package com.pixshow.framework.utils;

import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ $Rev$: $Date$:
 * @since Nov 8, 2012
 * 
 */

public class Base64Utility extends Base64 {

    private static final char   last2byte   = (char) Integer.parseInt("00000011", 2);
    private static final char   last4byte   = (char) Integer.parseInt("00001111", 2);
    private static final char   last6byte   = (char) Integer.parseInt("00111111", 2);
    private static final char   lead6byte   = (char) Integer.parseInt("11111100", 2);
    private static final char   lead4byte   = (char) Integer.parseInt("11110000", 2);
    private static final char   lead2byte   = (char) Integer.parseInt("11000000", 2);
    /**
     * "+" -> "*"; "/" -> "-"
     */
    private static final char[] encodeTable = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '*', '-' };

    /**
     * Encodes binary data using a URL-safe variation of the base64 algorithm but does not chunk the output. <br>
     * The url-safe variation emits * and - instead of + and / characters.<br>
     * 
     * @param binaryData
     *            binary data to encode
     * @return String containing Base64 characters
     * @since 1.4
     */
    public static String encodeBase64URLSafeString2(byte[] from) {
        StringBuffer to = new StringBuffer((int) (from.length * 1.34) + 3);
        int num = 0;
        char currentByte = 0;
        for (int i = 0; i < from.length; i++) {
            num = num % 8;
            while (num < 8) {
                switch (num) {
                    case 0:
                        currentByte = (char) (from[i] & lead6byte);
                        currentByte = (char) (currentByte >>> 2);
                        break;
                    case 2:
                        currentByte = (char) (from[i] & last6byte);
                        break;
                    case 4:
                        currentByte = (char) (from[i] & last4byte);
                        currentByte = (char) (currentByte << 2);
                        if ((i + 1) < from.length) {
                            currentByte |= (from[i + 1] & lead2byte) >>> 6;
                        }
                        break;
                    case 6:
                        currentByte = (char) (from[i] & last2byte);
                        currentByte = (char) (currentByte << 4);
                        if ((i + 1) < from.length) {
                            currentByte |= (from[i + 1] & lead4byte) >>> 4;
                        }
                        break;
                }
                to.append(encodeTable[currentByte]);
                num += 6;
            }
        }
        if (to.length() % 4 != 0) {
            for (int i = 4 - to.length() % 4; i > 0; i--) {
                to.append("=");
            }
        }
        return to.toString();
    }

    public static void main(String[] args) {
        encodeBase64URLSafeString(null);
        System.out.println(UUID.randomUUID().toString());
    }
}
