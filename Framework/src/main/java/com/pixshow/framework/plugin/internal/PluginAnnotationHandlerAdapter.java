/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:SecurityAnnotationHandlerAdapter.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jun 1, 2012 9:40:56 AM
 * 
 */
package com.pixshow.framework.plugin.internal;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import com.pixshow.framework.annotation.api.AnnotationHandler;
import com.pixshow.framework.plugin.api.PluginRegisterManager;
import com.pixshow.framework.plugin.api.annotation.Plugin;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.2 $ $Date: 2012/07/12 09:33:51 $
 * @since Jun 1, 2012
 * 
 */
@Component
public class PluginAnnotationHandlerAdapter implements AnnotationHandler<Plugin> {

    @Override
    public Class<Plugin> getAnnotationType() {
        return Plugin.class;
    }

    @Override
    public void handleBeanDefinition(ConfigurableListableBeanFactory beanFactory, String beanName, BeanDefinition beanDefinition, Plugin annotationType) {
        PluginRegisterManager.getInstance().register(beanName, annotationType);
    }
}
