/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:FrameworkListener.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Mar 16, 2012 11:44:24 AM
 * 
 */
package com.pixshow.framework.launcher;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

/**
 * 
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.3 $ $Date: 2012/07/12 09:33:58 $
 * @since Mar 21, 2012
 * 
 */
public class FrameworkListener extends ContextLoaderListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        super.contextDestroyed(servletContextEvent);
    }

}
