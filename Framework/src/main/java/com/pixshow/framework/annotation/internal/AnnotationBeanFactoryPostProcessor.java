/*
 * Copyright (c) 2010-2012 www.pixshow.net All Rights Reserved
 *
 * File:AnnotationBeanFactoryPostProcessor.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Dec 27, 2012 5:03:14 PM
 * 
 */
package com.pixshow.framework.annotation.internal;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.pixshow.framework.annotation.api.AnnotationHandler;
import com.pixshow.framework.annotation.api.annotation.FrameworkComponent;

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
public class AnnotationBeanFactoryPostProcessor implements BeanFactoryPostProcessor, ApplicationContextAware {

    //    private Log log = LogFactory.getLog(getClass());

    private ApplicationContext              applicationContext;
    private ConfigurableListableBeanFactory beanFactory;

    List<String>                            annotationBeanNames       = new ArrayList<String>();
    List<Class<?>>                          annotationClasses         = new ArrayList<Class<?>>();
    List<BeanDefinition>                    annotationBeanDefinitions = new ArrayList<BeanDefinition>();

    List<String>                            handlerBeanNames          = new ArrayList<String>();

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            String beanClassName = beanDefinition.getBeanClassName();

            if (StringUtils.isEmpty(beanClassName)) {
                continue;
            }
            Class<?> cls = null;
            try {
                cls = ClassUtils.getClass(beanClassName);
            } catch (ClassNotFoundException e1) {
            }
            if (cls != null) {
                FrameworkComponent frameworkComponent = AnnotationUtils.findAnnotation(cls, FrameworkComponent.class);
                if (frameworkComponent != null) {
                    annotationBeanNames.add(beanName);
                    annotationClasses.add(cls);
                    annotationBeanDefinitions.add(beanDefinition);
                }

                if (AnnotationHandler.class.isAssignableFrom(cls)) {
                    handlerBeanNames.add(beanName);
                }
            }
        }

        process();
    }

    @SuppressWarnings("unchecked")
    private void process() {
        for (String handlerBeanName : handlerBeanNames) {
            AnnotationHandler handler = this.applicationContext.getBean(handlerBeanName, AnnotationHandler.class);
            for (int index = 0; index < annotationClasses.size(); index++) {
                Annotation annotationType = AnnotationUtils.findAnnotation(annotationClasses.get(index), handler.getAnnotationType());
                if (annotationType != null) {
                    handler.handleBeanDefinition(beanFactory, annotationBeanNames.get(index), annotationBeanDefinitions.get(index), annotationType);
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
