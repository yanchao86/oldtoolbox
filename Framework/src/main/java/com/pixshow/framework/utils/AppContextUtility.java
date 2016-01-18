/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:SpringUtility.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:May 18, 2012 1:41:59 PM
 * 
 */
package com.pixshow.framework.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.2 $ $Date: 2012/07/12 09:33:52 $
 * @since May 18, 2012
 * 
 */

@Component
public class AppContextUtility implements ApplicationContextAware {

    private static final String       SPRING_CONFIG      = "/spring.xml";

    private static ApplicationContext applicationContext = null;

    public static ApplicationContext getContext() {
        if (applicationContext == null) {
            loadLocalContext();
        }
        return applicationContext;
    }

    private static void loadLocalContext() {
        applicationContext = new ClassPathXmlApplicationContext(SPRING_CONFIG);
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }
}
