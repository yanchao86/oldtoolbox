/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:HttpUtilityTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Feb 25, 2013 4:29:46 PM
 * 
 */
package com.pixshow.framework.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$
 * @since Feb 25, 2013
 * 
 */
public class HttpUtilityTests {
    public static void main(String[] args) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        data.put("a", "11");
        data.put("b", "22");
        data.put("c", "33");
        for (int i = 0; i < 10; i++) {
            System.out.println(HttpUtility.post("http://10.88.1.211:8081/example2?id=1&name=aaa", data));
        }

        // System.out.println(HttpUtility.get("http://www.dict.cn/", null));
        // long time1 = System.currentTimeMillis();
        // String html =
        // HttpUtility.get("http://www.weather.com.cn/weather/101200703.shtml");
        // long time2 = System.currentTimeMillis();

        // System.out.println("耗时:" + (time2 - time1));
        // System.out.println(html);
    }
}
