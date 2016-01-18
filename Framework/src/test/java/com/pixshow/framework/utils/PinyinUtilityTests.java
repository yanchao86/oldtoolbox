/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:PinyinUtilityTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Feb 26, 2013 2:38:48 PM
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
 * @since Feb 26, 2013
 * 
 */

public class PinyinUtilityTests {
    public static void main(String[] args) {
        System.out.println(PinyinUtility.toPinyin("中国汉字"));
        System.out.println(PinyinUtility.toPinyin("中国汉字 ABC"));
        System.out.println(PinyinUtility.toPinyinInitial("中国汉字"));
        System.out.println(PinyinUtility.toPinyinInitial("中国汉字 ABC"));

        System.out.println(PinyinUtility.toPinyinInitial("繁體漢字 ABC"));
    }
}
