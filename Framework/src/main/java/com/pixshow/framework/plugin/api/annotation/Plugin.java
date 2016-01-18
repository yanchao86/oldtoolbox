/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:Spi.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jun 20, 2012 4:52:14 PM
 * 
 */
package com.pixshow.framework.plugin.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.pixshow.framework.annotation.api.annotation.FrameworkComponent;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.3 $ $Date: 2012/07/27 02:29:51 $
 * @since Jun 20, 2012
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@FrameworkComponent
public @interface Plugin {

    String name() default "";

    Class<?> type();

    int priority() default 10;

    PluginParameter[] parameters() default {};

}
