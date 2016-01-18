/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:Tests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:May 28, 2013 6:11:06 PM
 * 
 */
package com.pixshow.framework.utils;

import java.util.Map;

import com.pixshow.framework.config.Config;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since May 28, 2013
 * 
 */

public class AndroidPushUtility {
    public static BaiduPush baiduPush = new BaiduPush(BaiduPush.HTTP_METHOD_POST, Config.getInstance().getString("push.baidu.secritkey"), Config.getInstance().getString("push.baidu.appkey"));

    public static String push(String title, String message, String userId) {
        return baiduPush.PushNotify(title, message, userId);
    }

    public static String push(String title, String message, Map<String, String> custom_content, String userId) {
        return baiduPush.PushNotify(title, message, userId, custom_content);
    }

    public static String pushTag(String title, String message, String tag) {
        return baiduPush.PushTagNotify(title, message, null, tag);
    }

    public static String pushTag(String title, String message, Map<String, String> custom_content, String tag) {
        return baiduPush.PushTagNotify(title, message, custom_content, tag);
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    public static String pushMessage(String title, String message, String userId) {
        return baiduPush.PushMessage(title, message, userId);
    }

    public static String pushMessage(String title, String message, Map<String, String> custom_content, String userId) {
        return baiduPush.PushMessage(title, message, custom_content, userId);
    }

    public static String pushTagMessage(String title, String message, String tag) {
        return baiduPush.PushTagMessage(title, message, null, tag);
    }

    public static String pushTagMessage(String title, String message, Map<String, String> custom_content, String tag) {
        return baiduPush.PushTagMessage(title, message, custom_content, tag);
    }

    public static void setTag(String tag, String... userid) {
        for (int i = 0; i < userid.length; i++) {
            baiduPush.SetTag(tag, userid[i]);
        }
    }

}
