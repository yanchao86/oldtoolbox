/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved.
 *
 * File:FrameworkComponent.java Project: Framework
 * 
 * Creator:<a href=mailto:liangqingming@300.cn>Liangqingming</a> 
 * Date:May 4, 2012 10:38:23 AM
 * 
 */
package com.pixshow.framework.annotation.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * 框架组件标识注解，用于自动扫描.
 * 
 * @author <a href=mailto:liangqingming@300.cn>Liangqingming</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.2 $ $Date: 2012/07/12 09:33:58 $
 * @since May 4, 2012
 */
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface FrameworkComponent {
}