/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:Init.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Dec 3, 2013 11:09:16 AM
 * 
 */
package com.pixshow.framework.launcher.plugin;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;

import com.pixshow.framework.ddb.api.DDBCentextHolder;
import com.pixshow.framework.dtask.api.DTaskManager;
import com.pixshow.framework.launcher.FilterPlugin;
import com.pixshow.framework.plugin.api.annotation.Plugin;
import com.pixshow.framework.thread.api.ThreadManager;
import com.pixshow.framework.utils.WebUtility;

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

@Plugin(type = FilterPlugin.class, priority = 110)
public class InitFilterPlugin implements FilterPlugin {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        WebUtility.setServletContext(filterConfig.getServletContext());
    }

    @Override
    public void destroy() {
        ThreadManager.getInstance().shutdown();
        DTaskManager.shutdown();
        LogManager.shutdown();
    }

    @Override
    public boolean beginDoFilter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        DDBCentextHolder.initThreadLocal();
        return CONTINUE;
    }

    @Override
    public void afterDoFilter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

}
