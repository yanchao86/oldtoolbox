/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:WallpaperTootls.java Project: WallpaperManager
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Dec 6, 2013 1:58:52 PM
 * 
 */
package com.pixshow.toolboxmgr.tools;

import java.io.File;

import com.pixshow.framework.config.Config;
import com.pixshow.framework.storage.api.StorageManager;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$
 * @since Dec 6, 2013
 * 
 */

public class ImageStorageTootl {

    public static String getUrl_bak(String fileName) {
        String appName = Config.getInstance().getString("aliyun.oss.appName", "error");
        return "http://vimg.oss.aliyuncs.com/" + appName + "/" + fileName;
    }

    public static String getUrl_bak(String fileName, String width) {
        String appName = Config.getInstance().getString("aliyun.oss.appName", "error");
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        String type = fileName.substring(fileName.lastIndexOf("."));
        String imageFileName = name + "_" + width + type;
        return "http://vimg.oss.aliyuncs.com/" + appName + "/" + imageFileName;
    }

    public static String upload_bak(String fileName, File file) {
        String appName = Config.getInstance().getString("aliyun.oss.appName", "error");
        return StorageManager.getStorage().upload("vimg", appName + "/" + fileName, file, "image/jpeg");
    }

    public static String getUrl(String fileName) {
        return Config.getInstance().getString("toolbox.img.url") + Config.getInstance().getString("toolbox.img.folder") + fileName;
    }

    public static String getUrl(String fileName, String width) {
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        String type = fileName.substring(fileName.lastIndexOf("."));
        String imageFileName = name + "_" + width + type;
        return Config.getInstance().getString("toolbox.img.url") + Config.getInstance().getString("toolbox.img.folder") + imageFileName;
    }

}
