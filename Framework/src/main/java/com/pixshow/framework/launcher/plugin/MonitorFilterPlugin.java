/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:ReuqestMonitor.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Dec 3, 2013 11:02:26 AM
 * 
 */
package com.pixshow.framework.launcher.plugin;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
@Plugin(type = FilterPlugin.class, priority = 120)
public class MonitorFilterPlugin implements FilterPlugin {

    private Log log = LogFactory.getLog(getClass());

    private int expenseThreshold = -1;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (StringUtility.isNotEmpty(filterConfig.getInitParameter("expenseThreshold"))) {
            this.expenseThreshold = NumberUtils.toInt(filterConfig.getInitParameter("expenseThreshold"));
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public boolean beginDoFilter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("__requestDate__", System.currentTimeMillis());
        return CONTINUE;
    }

    @Override
    public void afterDoFilter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Long requestDate = (Long) request.getAttribute("__requestDate__");
        if (requestDate == null) { return; }
        long responseDate = System.currentTimeMillis();
        long consume = responseDate - requestDate;
        if (expenseThreshold != -1 && consume >= expenseThreshold) {
            StringBuilder msg = new StringBuilder("===== [" + consume + " ms] ===== " + request.getMethod() + " ===== ");
            msg.append(request.getRequestURL().toString());
            if (request.getQueryString() != null) {
                msg.append("?");
                msg.append(request.getQueryString());
            }
            log.info(msg.toString());
        }
    }

}
