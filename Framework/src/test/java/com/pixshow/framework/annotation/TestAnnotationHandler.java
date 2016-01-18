/*
 * Copyright (c) 2010-2012 www.pixshow.net All Rights Reserved
 *
 * File:TestAnnotationAnnotationHandler.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Dec 27, 2012 7:19:55 PM
 * 
 */
package com.pixshow.framework.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import com.pixshow.framework.annotation.api.AnnotationHandler;

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

@Component
public class TestAnnotationHandler implements AnnotationHandler<TestAnnotation> {

    @Override
    public Class<TestAnnotation> getAnnotationType() {
        return TestAnnotation.class;
    }

    @Override
    public void handleBeanDefinition(ConfigurableListableBeanFactory beanFactory, String beanName, BeanDefinition beanDefinition, TestAnnotation annotationType) {
    }

}
