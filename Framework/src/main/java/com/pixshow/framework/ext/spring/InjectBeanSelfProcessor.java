/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:InjectBeanSelfProcessor.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Aug 8, 2012 11:44:51 AM
 * 
 */
package com.pixshow.framework.ext.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 
 * 代理对象的注入 解决 内部调用 不走 AOP的问题。
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.3 $ $Date: 2012/10/29 02:53:43 $
 * @since Aug 8, 2012
 * 
 */
@Component
public class InjectBeanSelfProcessor implements BeanPostProcessor, Ordered {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof BeanSelfAware) {
            ((BeanSelfAware) bean).setThis(bean);
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
