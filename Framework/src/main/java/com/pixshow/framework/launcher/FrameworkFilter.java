/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:FrameworkFilter.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jul 16, 2012 10:21:48 AM
 * 
 */
package com.pixshow.framework.launcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pixshow.framework.plugin.api.PluginDefinition;
import com.pixshow.framework.plugin.api.PluginRegisterManager;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: yangjunshuai.300.cn $
 * @version $Revision: 1.4 $ $Date: 2012/10/24 06:47:58 $
 * @since Jul 16, 2012
 * 
 */

public class FrameworkFilter implements Filter {

    private Log                log     = LogFactory.getLog(getClass());

    private List<FilterPlugin> plugins = new ArrayList<FilterPlugin>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        List<PluginDefinition> pluginDefinitions = PluginRegisterManager.getInstance().getDefinitions(FilterPlugin.class);
        for (PluginDefinition pluginDefinition : pluginDefinitions) {
            FilterPlugin plugin = PluginRegisterManager.getInstance().getPlugin(pluginDefinition);
            plugin.init(filterConfig);
            log.info(" 加载Filter插件 " + plugin.getClass().getName());
            plugins.add(plugin);
        }
    }

    @Override
    public void destroy() {
        for (FilterPlugin plugin : plugins) {
            try {
                plugin.destroy();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            for (FilterPlugin plugin : plugins) {
                if (!plugin.beginDoFilter((HttpServletRequest) request, (HttpServletResponse) response)) {
                    return;
                }
            }
            chain.doFilter(request, response);
        } finally {
            for (FilterPlugin plugin : plugins) {
                plugin.afterDoFilter((HttpServletRequest) request, (HttpServletResponse) response);
            }
        }
    }
}
