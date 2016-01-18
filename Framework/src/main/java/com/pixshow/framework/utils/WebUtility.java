/*
 * Copyright (c) 2010-2012 www.pixshow.net All Rights Reserved
 *
 * File:WebUtility.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Nov 26, 2012 11:00:47 AM
 * 
 */
package com.pixshow.framework.utils;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$
 * @since Nov 26, 2012
 * 
 */

public class WebUtility {

    private static ServletContext servletContext = null;
    private static String         webRoot        = null;

    public static void setServletContext(ServletContext sc) {
        servletContext = sc;
        init();
    }

    private static void init() {
        webRoot = servletContext.getRealPath("/");
    }

    private static String localWebRootPath() {
        try {
            File folder = new File(WebUtility.class.getResource("/").toURI());
            while (folder != null && !folder.getName().equals("WEB-INF")) {
                folder = folder.getParentFile();
            }

            if (folder != null && folder.getName().equals("WEB-INF")) {
                return folder.getParentFile().getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getWebRootPath() {
        if (webRoot != null) {
            return webRoot;
        }
        return localWebRootPath();
    }

    public static String getBasePath(HttpServletRequest request) {
        String xHasContextPath = request.getHeader("X-Has-ContextPath");
        String path = "true".equalsIgnoreCase(xHasContextPath) ? "" : request.getContextPath();
        path = path.endsWith("/") ? path : (path + "/");
        String basePath = request.getScheme() + "://" + request.getServerName() + (request.getServerPort() == 80 ? "" : ":" + request.getServerPort()) + path;
        return basePath;
    }

    public static void main(String[] args) {
        System.out.println(getWebRootPath());
    }

}
