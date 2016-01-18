/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:QuartzJob.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jul 2, 2012 2:36:52 PM
 * 
 */
package com.pixshow.framework.schedule.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.3 $ $Date: 2012/09/21 02:09:14 $
 * @since Jul 2, 2012
 * 
 */

@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ScheduleJob {

    String name();

    String cron();

    boolean enable() default true;
}
