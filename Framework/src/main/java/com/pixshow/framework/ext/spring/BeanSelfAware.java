/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:BeanSelfAware.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Aug 8, 2012 11:45:53 AM
 * 
 */
package com.pixshow.framework.ext.spring;

/**
 * 
 * 代理对象的注入 解决 内部调用 不走 AOP的问题。
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.2 $ $Date: 2012/10/29 02:53:43 $
 * @since Aug 8, 2012
 * 
 */

public interface BeanSelfAware {

    public Object getThis();

    public void setThis(Object proxy);

}
