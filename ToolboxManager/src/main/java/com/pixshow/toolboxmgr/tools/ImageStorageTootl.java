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
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.httpclient.methods.multipart.FilePart;

import com.pixshow.framework.config.Config;
import com.pixshow.framework.storage.api.StorageManager;
import com.pixshow.framework.utils.HttpUtility;

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
        String url = Config.getInstance().getString("aliyun.oss.imageUrl");
        return url + appName + "/" + fileName;
    }

    public static String getUrl_bak(String fileName, String width) {
        String appName = Config.getInstance().getString("aliyun.oss.appName", "error");
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        String type = fileName.substring(fileName.lastIndexOf("."));
        String imageFileName = name + "_" + width + type;
        String url = Config.getInstance().getString("aliyun.oss.imageUrl");
        return url + appName + "/" + imageFileName;
    }

    public static String upload_bak(String fileName, File file) {
        String appName = Config.getInstance().getString("aliyun.oss.appName", "error");
        return StorageManager.getStorage().upload("vimg", appName + "/" + fileName, file, "image/jpeg");
    }

    public static String getUrl(String fileName) {
        return getUrl(fileName, Config.getInstance().getString("toolbox.img.folder"));
    }

    public static String getUrl(String fileName, String folder) {
        return Config.getInstance().getString("toolbox.img.url") + folder + "/" + fileName;
    }

    public static String getUrl(String fileName, String folder, String width) {
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        String type = fileName.substring(fileName.lastIndexOf("."));
        String imageFileName = name + "_" + width + type;
        return Config.getInstance().getString("toolbox.img.url") + folder + imageFileName;
    }

    public static String upload(String fileName, File file) {
        return upload(fileName, Config.getInstance().getString("toolbox.img.folder"), file);
    }

    public static String upload(String fileName, String folder, File file) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("fileName", fileName);
        params.put("folder", folder);
        List<FilePart> files = new ArrayList<FilePart>();
        try {
            files.add(new FilePart("file", file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        HttpUtility.upload("http://mgr-oss.tv163.com/upload.jsp?scerectKey=DSJK164dsaDLD", params, files);
        
        HttpUtility.upload(Config.getInstance().getString("toolbox.uploadUrl")+"upload.jsp?scerectKey=DSJK164dsaDLD", params, files);
        return getUrl(fileName, folder);
    }

}
