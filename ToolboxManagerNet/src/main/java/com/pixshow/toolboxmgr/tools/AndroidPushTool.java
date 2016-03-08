/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:Tests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:May 28, 2013 6:11:06 PM
 * 
 */
package com.pixshow.toolboxmgr.tools;

import java.util.Map;

import com.pixshow.framework.utils.BaiduPush;
import com.pixshow.framework.utils.StringUtility;

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

public class AndroidPushTool {
    public BaiduPush baiduPush = null;

    public AndroidPushTool(String appkey, String secritkey) {
        if (StringUtility.isNotEmpty(secritkey) && StringUtility.isNotEmpty(appkey)) {
            baiduPush = new BaiduPush(BaiduPush.HTTP_METHOD_POST, secritkey, appkey);
        }
    }

    public String push(String title, String message, String userId) {
        if (baiduPush == null) { return null; }
        return baiduPush.PushNotify(title, message, userId);
    }

    public String push(String title, String message, Map<String, String> custom_content, String userId) {
        if (baiduPush == null) { return null; }
        return baiduPush.PushNotify(title, message, userId, custom_content);
    }

    public String pushTag(String title, String message, String tag) {
        if (baiduPush == null) { return null; }
        return baiduPush.PushTagNotify(title, message, null, tag);
    }

    public String pushTag(String title, String message, Map<String, String> custom_content, String tag) {
        if (baiduPush == null) { return null; }
        return baiduPush.PushTagNotify(title, message, custom_content, tag);
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    public String pushMessage(String title, String message, String userId) {
        if (baiduPush == null) { return null; }
        return baiduPush.PushMessage(title, message, userId);
    }

    public String pushMessage(String title, String message, Map<String, String> custom_content, String userId) {
        if (baiduPush == null) { return null; }
        return baiduPush.PushMessage(title, message, custom_content, userId);
    }

    public String pushTagMessage(String title, String message, String tag) {
        if (baiduPush == null) { return null; }
        return baiduPush.PushTagMessage(title, message, null, tag);
    }

    public String pushTagMessage(String title, String message, Map<String, String> custom_content, String tag) {
        if (baiduPush == null) { return null; }
        return baiduPush.PushTagMessage(title, message, custom_content, tag);
    }

    public void setTag(String tag, String... userid) {
        if (baiduPush == null) { return; }
        for (int i = 0; i < userid.length; i++) {
            baiduPush.SetTag(tag, userid[i]);
        }
    }

}
