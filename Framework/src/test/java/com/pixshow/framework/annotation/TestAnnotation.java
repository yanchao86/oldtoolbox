/*
 * Copyright (c) 2010-2012 www.pixshow.net All Rights Reserved
 *
 * File:TestAnnotation.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Dec 27, 2012 7:19:16 PM
 * 
 */
package com.pixshow.framework.annotation;

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
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Dec 27, 2012
 * 
 */
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@FrameworkComponent
public @interface TestAnnotation {
    String name();
}
