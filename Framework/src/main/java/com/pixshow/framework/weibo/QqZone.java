/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:QqZone.java Project: LvFramework
 * 
 * Creator:4399-lvtu-8 
 * Date:Jun 4, 2013 10:23:59 AM
 * 
 */
package com.pixshow.framework.weibo;

import com.qq.connect.api.qzone.Share;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Jun 4, 2013
 * 
 */

public class QqZone {
    public static String addShare(String token, String openId, String image, String comment) {
        String result = "ok";
        comment = comment == null || "".equals(comment) ? "来自拍秀" : comment;
        try {
            Share share = new Share(token, openId);
            result = share.addShare("拍秀",//1 
                    image,//2 
                    "pixshow", //3
                    "http://www.6288.com",//4
                    "comment=" + comment,//5
                    "type=4",//6
                    "summary=每个人在旅程中都会从不同的角度来记录着快乐和幸福。淡淡的、激情的、灿烂的。跟随好友的足迹，探寻旅程中不同的精彩。",//7
                    "images=" + image, 
                    "nswb=1").toString();
        } catch (Exception e) {
            result = e.getMessage();
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(addShare("F1EFC4A64273CE294A6C4C7CB94259D9", 
                "86BC94521E85C36A5B079EDE0C514003", 
                "http://g.hiphotos.baidu.com/album/w%3D2048/sign=af9c37f177094b36db921ced97f47cd9/e1fe9925bc315c60aa99a2c18cb1cb1349547794.jpg", 
                "哼哼哈嘿~~"));
    }
}
