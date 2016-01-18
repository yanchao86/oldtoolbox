/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:QqWeibo.java Project: LvFramework
 * 
 * Creator:4399-lvtu-8 
 * Date:Jun 4, 2013 10:23:49 AM
 * 
 */
package com.pixshow.framework.weibo;

import com.tencent.weibo.api.TAPI;
import com.tencent.weibo.oauthv1.OAuthV1;
import com.tencent.weibo.oauthv2.OAuthV2;

/**
 * 
 * 支持OAuthV1和OAuthV2
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Jun 4, 2013
 * 
 */

public class QqWeibo {
    /**
     * OAuthV2
     * @param token
     * @param openid
     * @param refreshToken
     * @param title
     * @param image
     * @return
     *
     */
    public static String addPic(String token, String openid, String refreshToken, String title, String image) {
        title = title == null || "".equals(title) ? "来自拍秀" : title;
        String result = "ok";
        OAuthV2 oAuth = new OAuthV2();
        oAuth.setClientId("801088023");
        oAuth.setClientSecret("73e3e3be2f113d22709711085eea7a16");
        oAuth.setRedirectUri("http://www.pixshow.net");
        oAuth.setAccessToken(token);
        oAuth.setOpenid(openid);
        oAuth.setRefreshToken(refreshToken);
        TAPI api = new TAPI(oAuth.getOauthVersion());
        try {
            result = api.addPic(oAuth, "json", title, "127.0.0.1", image);
        } catch (Exception e) {
            result = e.getMessage();
            e.printStackTrace();
        } finally {
            api.shutdownConnection();
        }
        return result;
    }

    /**
     * OAuthV1
     * @param token
     * @param secret
     * @param title
     * @param image
     * @return
     *
     */
    public static String addPic(String token, String secret, String title, String image) {
        title = title == null || "".equals(title) ? "来自拍秀" : title;
        String result = "ok";
        OAuthV1 oAuth1 = new OAuthV1();
        oAuth1.setOauthConsumerKey("801088023");
        oAuth1.setOauthConsumerSecret("73e3e3be2f113d22709711085eea7a16");

        oAuth1.setOauthToken(token); //token
        oAuth1.setOauthTokenSecret(secret); //token_secret
        TAPI api = new TAPI(oAuth1.getOauthVersion());
        try {
            result = api.addPic(oAuth1, "json", title, "127.0.0.1", image);
        } catch (Exception e) {
            result = e.getMessage();
            e.printStackTrace();
        } finally {
            api.shutdownConnection();
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(addPic("ac23e6a99b6a46f6bbe816fa7548691e", //
                "05281a06e73cfb290e376f19db9ba955", //
                "乌鲁木齐", //
                "http://img1.2828.net/img/2013/0604/66/1681711370333966.jpg"));
    }
}
