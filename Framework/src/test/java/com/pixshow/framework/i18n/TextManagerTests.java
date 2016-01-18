/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:TextManagerTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:May 7, 2013 2:22:51 PM
 * 
 */
package com.pixshow.framework.i18n;

import java.util.HashMap;
import java.util.Map;

import com.pixshow.framework.i18n.api.TextManager;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since May 7, 2013
 * 
 */

public class TextManagerTests {
    public static void main(String[] args) {
        System.out.println(TextManager.getString("key1", "name"));
        System.out.println(TextManager.getString("key1", "name", "en_us"));
        System.out.println(TextManager.getString("key1", "name", "zh_tw"));
        System.out.println(TextManager.getString("key1", "name", "ja_jp"));
        //        System.out.println(TextManager.getString("key1", "name", "zh_hans"));

        Map<String, Object> parms = new HashMap<String, Object>();
        parms.put("k1", "A");
        parms.put("k2", "B");
        parms.put("k3", "C");
        System.out.println(TextManager.getString("key1", "name", "zh_TW", parms));

        //        Locale locale = new Locale("zh_CN");
        //        ResourceBundle bundle = ResourceBundle.getBundle("i18n/key1", locale);
        //        System.out.println(bundle.getString("name"));
    }
}
