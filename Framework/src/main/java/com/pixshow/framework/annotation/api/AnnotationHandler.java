/*
 * Copyright (c) 2010-2012 www.pixshow.net All Rights Reserved
 *
 * File:AnnotationHandler2.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Dec 27, 2012 5:00:36 PM
 * 
 */
package com.pixshow.framework.annotation.api;

import java.lang.annotation.Annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Dec 27, 2012
 * 
 */

public interface AnnotationHandler<T extends Annotation> {

    public void handleBeanDefinition(ConfigurableListableBeanFactory beanFactory, String beanName, BeanDefinition beanDefinition, T annotationType);

    public Class<T> getAnnotationType();
}
