/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:UploadFileCleanup.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Dec 3, 2013 11:26:09 AM
 * 
 */
package com.pixshow.framework.launcher.plugin;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.pixshow.framework.launcher.FilterPlugin;
import com.pixshow.framework.plugin.api.annotation.Plugin;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Dec 3, 2013
 * 
 */
@Plugin(type = FilterPlugin.class, priority = 150)
public class UploadFileCleanupFilterPlugin implements FilterPlugin {

    private Log log = LogFactory.getLog(getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public boolean beginDoFilter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        return CONTINUE;
    }

    @Override
    public void afterDoFilter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (request instanceof MultiPartRequestWrapper) {
            MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) request;
            Enumeration<String> fileParameterNames = multiWrapper.getFileParameterNames();
            while (fileParameterNames != null && fileParameterNames.hasMoreElements()) {
                String fieldName = fileParameterNames.nextElement();
                File[] files = multiWrapper.getFiles(fieldName);
                for (File currentFile : files) {
                    if ((currentFile != null) && currentFile.isFile()) {
                        boolean successfully = currentFile.delete();
                        if (log.isDebugEnabled()) {
                            log.debug("删除文件 " + successfully + " -> " + currentFile.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }
}
