/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:BaseService.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Nov 8, 2012 12:17:47 PM
 * 
 */
package com.pixshow.framework.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pixshow.framework.ext.spring.BeanSelfAware;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Nov 8, 2012
 * 
 */

public abstract class BaseService implements BeanSelfAware {

    protected Log  log   = LogFactory.getLog(getClass());
    private Object proxy = null;

    @Override
    public void setThis(Object proxy) {
        this.proxy = proxy;
    }

    @Override
    public Object getThis() {
        return proxy;
    }
}
