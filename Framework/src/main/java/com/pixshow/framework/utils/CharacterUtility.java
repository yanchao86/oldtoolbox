/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:CharacterUtility.java Project: TestWeb
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Apr 8, 2013 7:15:52 PM
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
 * @since Apr 8, 2013
 * 
 */

public class CharacterUtility {
    public static String codePointToUTF16(int v) {
        if (v < 0x10000) {
            return Character.toString((char) v);
        }
        int vp = v - 0x10000;
        int vh = vp >> 10;
        int vl = vp & 0x3FF;
        int w1 = 0xD800 + vh;
        int w2 = 0xDC00 + vl;
        return Character.toString((char) w1) + Character.toString((char) w2);
    }

}
