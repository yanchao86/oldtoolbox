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

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpUtils;

import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.net.util.Base64;

import net.sf.json.JSONObject;

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
    public static void main1(String[] args) throws Exception {
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

    public static void main(String[] args) {
        String json = "{\"multi\":true,\"app_info\":{\"app_id\":\"a9f088e7\",\"app_version\":\"0.14\",\"ad_type_id\":\"2442639\","//
                + "\"ad_size\":\"360x300\",\"host_package_name\":\"com.mojang.minecraftpe\"},\"device_info\":{\"imei\":\"866926020248380\","//
                + "\"imsi\":\"460011901617139\",\"android_id\":\"160e18a715ccea95\",\"mac\":\"d8:55:a3:ce:e4:40\",\"device_type\":1,\"os\":1,"//
                + "\"os_version\":\"5.1\",\"vendor\":\"nubia\",\"model\":\"NX510J\",\"screen_size\":\"1080x1920\"},"//
                + "\"network_info\":{\"ip\":\"223.223.197.35\",\"connetion_type\":100}}";
        System.out.println("json = " + json);
        String b = Base64.encodeBase64URLSafeString(json.getBytes());
        System.out.println(b);

        for (int i = 0; i < 10; i++) {
            long time1 = System.currentTimeMillis();
            String res = HttpUtility.get("http://openapi.91shoufu.com/get/api?json=" + b);
            long time2 = System.currentTimeMillis();
            System.out.println("耗时:" + (time2 - time1) + " 毫秒");
            System.out.println(res);    
        }
        
    }

}
