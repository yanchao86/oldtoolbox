/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:SinaWeibo.java Project: LvFramework
 * 
 * Creator:4399-lvtu-8 
 * Date:Jun 4, 2013 10:23:33 AM
 * 
 */
package com.pixshow.framework.weibo;

import java.io.File;
import java.net.URLEncoder;

import weibo4j.Timeline;
import weibo4j.http.ImageItem;
import weibo4j.model.Status;

import com.pixshow.framework.utils.FileUtility;
import com.pixshow.framework.utils.HttpUtility;

/**
 * 
 * 仅支持sina微博2.0
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Jun 4, 2013
 * 
 */

public class SinaWeibo {

    public static String uploadStatus(String token, String image, String content) {
        return uploadStatus(token, image, content, null, null);
    }

    public static String uploadStatus(String token, String image, String content, Float lat, Float lng) {
        content = content == null ? "来自拍秀" : content;
        String result = "ok";
        try {
            File file = null;
            if (image.startsWith("http://")) {
                file = HttpUtility.download(image);
            } else {
                file = new File(image);
            }
            if (content != null && content.length() > 140) {
                content = content.substring(0, 140);
            }
            if (file != null) {
                byte[] imageData = FileUtility.readFileToByteArray(file);
                ImageItem pic = new ImageItem("pic", imageData);
                String s = URLEncoder.encode(content, "utf-8");
                Timeline t = new Timeline();
                t.client.setToken(token);//token
                Status status = null;
                if (lat == null || lng == null) {
                    status = t.UploadStatus(s, pic);
                    
                } else {
                    status = t.UploadStatus(s, pic, lat, lng);
                }
                if(status != null) {
                    result = status.toString();
                }
            } else {
                result = "no file";
            }
        } catch (Exception e) {
            result = e.getMessage();
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(uploadStatus("2.00XnYNoBAABLYE78d59b253aetXVBC", 
                "http://img1.2828.net/img/2013/0604/89/1304841370332789.jpg",
                null));
    }
}
