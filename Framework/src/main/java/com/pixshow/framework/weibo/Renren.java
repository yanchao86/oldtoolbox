/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:Renren.java Project: LvFramework
 * 
 * Creator:4399-lvtu-8 
 * Date:Jun 4, 2013 10:24:06 AM
 * 
 */
package com.pixshow.framework.weibo;

import com.renren.api.client.RenrenApiClient;
import com.renren.api.client.param.impl.AccessToken;

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

public class Renren {
    public static String publicFeed(String token, String title, String image) {
        title = title == null || "".equals(title) ? "来自拍秀" : title;
        RenrenApiClient client = RenrenApiClient.getInstance();
        return client.getFeedService().publicFeed("拍秀", //name
                "每个人在旅程中都会从不同的角度来记录着快乐和幸福。淡淡的、激情的、灿烂的。跟随好友的足迹，探寻旅程中不同的精彩。", //description
                image, //url
                image, //image
                "pixshow.net", //caption
                "来自拍秀", //action_name
                "http://www.pixshow.net/",//action_link
                title, //message
                new AccessToken(token)//token
                ).toString();
    }

    public static void main(String[] args) {
        System.out.println(publicFeed("176629|6.be8dd2623a2af66b4e066498b7f03024.2592000.1371351600-75166656", //
                "两只小鸭子~", //
                "http://f.hiphotos.baidu.com/album/w%3D2048/sign=5e47372c8644ebf86d71633fedc1d72a/5882b2b7d0a20cf4048af0f077094b36acaf9982.jpg"));
    }
}
