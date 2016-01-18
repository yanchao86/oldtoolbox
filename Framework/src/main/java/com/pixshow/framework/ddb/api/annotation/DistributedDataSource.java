/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:DynamicDataSource.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Aug 6, 2012 3:07:07 PM
 * 
 */
package com.pixshow.framework.ddb.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.1 $ $Date: 2012/08/09 07:22:03 $
 * @since Aug 6, 2012
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.TYPE })
public @interface DistributedDataSource {

    String dbName() default "";

    String schema() default "";

    String rule() default "";

    String[] args() default {};
}
