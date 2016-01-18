/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:Encoding.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Dec 3, 2013 11:08:54 AM
 * 
 */
package com.pixshow.framework.launcher.plugin;

import java.io.IOException;

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
 * @since Dec 3, 2013
 * 
 */

@Plugin(type = FilterPlugin.class, priority = 100)
public class EncodingFilterPlugin implements FilterPlugin {

    private String encoding = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (StringUtility.isNotEmpty(filterConfig.getInitParameter("encoding"))) {
            this.encoding = filterConfig.getInitParameter("encoding");
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public boolean beginDoFilter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding(encoding);
        response.setCharacterEncoding(encoding);
        return CONTINUE;
    }

    @Override
    public void afterDoFilter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }

}
