/*
 * Copyright (c) 2010-2014 www.pixshow.net All Rights Reserved
 *
 * File:PushTest.java Project: ToolboxManager
 * 
 * Creator:4399-lvtu-8 
 * Date:2014年1月6日 下午3:45:45
 * 
 */
package com.pixshow;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.pixshow.toolboxmgr.tools.AndroidPushTool;


/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since 2014年1月6日
 */
public class PushTest {

    public static void main1(String[] args) {
        JSONObject data = JSONObject.fromObject("{\"switch\":\"1\",\"content\":\"描述吧\",\"time\":\"20140312\",\"group\":{\"groupPic\":[\"1\",\"2\"],\"count\":\"12222\",\"cover\":\"xxx.jpg\"}}");
        String k = "group.cover";
        if(k.contains(".")) {
            System.out.println(data.get(k));
        }
    }
    
    public static void main(String[] args) {
        Map<String, String> custom = new HashMap<String, String>();
        custom.put("type", "1");
        System.out.println(new AndroidPushTool("jflXWjyPsSQ513mlAuHLKiSX", "MTwhbli9POAxaaPLSfx9YDI5mmTeNK2N").pushMessage("小工具推送", "zzzzzzzzzzzzz", custom, "1086399614784420048"));
    }
}
