/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:ClassFullNameAnnotationBeanNameGenerator.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jun 28, 2012 5:00:38 PM
 * 
 */
package com.pixshow.framework.ext.spring;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.util.StringUtils;

/**
 * 
 * 默认注解Bean名字。
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.3 $ $Date: 2012/10/29 02:53:43 $
 * @since Jun 28, 2012
 * 
 */

public class ClassFullNameAnnotationBeanNameGenerator extends AnnotationBeanNameGenerator {
    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        String beanName = null;
        if (definition instanceof AnnotatedBeanDefinition) {
            beanName = determineBeanNameFromAnnotation((AnnotatedBeanDefinition) definition);
        }
        if (!StringUtils.hasText(beanName)) {
            beanName = definition.getBeanClassName();
        }
        return beanName;
    }
}
