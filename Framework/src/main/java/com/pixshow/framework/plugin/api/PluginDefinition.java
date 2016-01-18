/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:SpiBeanDefinition.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jun 21, 2012 10:13:21 AM
 * 
 */
package com.pixshow.framework.plugin.api;

import com.pixshow.framework.plugin.api.annotation.Plugin;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.2 $ $Date: 2012/07/12 09:33:56 $
 * @since Jun 21, 2012
 * 
 */

public class PluginDefinition {
    private String beanName;
    private Plugin annotation;

    public PluginDefinition(String beanName, Plugin annotation) {
        this.beanName = beanName;
        this.annotation = annotation;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Plugin getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Plugin annotation) {
        this.annotation = annotation;
    }
}
