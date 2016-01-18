/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:FilterPlugin.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Dec 3, 2013 11:03:58 AM
 * 
 */
package com.pixshow.framework.launcher;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

public interface FilterPlugin {

    public static final boolean CONTINUE = true;

    public static final boolean END      = false;

    public void init(FilterConfig filterConfig) throws ServletException;

    public void destroy();

    public boolean beginDoFilter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

    public void afterDoFilter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
}
