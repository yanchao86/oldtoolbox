/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:AuthenticateFilter.java Project: LvPhotoScenery
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Mar 4, 2013 2:44:13 PM
 * 
 */
package com.pixshow.login.plugin;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pixshow.framework.launcher.FilterPlugin;
import com.pixshow.framework.plugin.api.annotation.Plugin;
import com.pixshow.framework.utils.StringUtility;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$
 * @since Mar 4, 2013
 * 
 */

@Plugin(type = FilterPlugin.class, priority = 200)
public class AuthenticateFilterPlugin implements FilterPlugin {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public boolean beginDoFilter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String login = (String) request.getSession().getAttribute("login");
        String uri = request.getRequestURI();
        if (uri.endsWith("/login.jsp") || uri.endsWith("/login.do") || "ok".equals(login)) { return CONTINUE; }

        String url = request.getRequestURL().toString();
        if (StringUtility.isNotEmpty(request.getQueryString())) {
            url += "?" + request.getQueryString();
        }
        response.sendRedirect(request.getContextPath() + "/login.jsp?url=" + URLEncoder.encode(url, "UTF-8"));
        return END;
    }

    @Override
    public void afterDoFilter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

}
