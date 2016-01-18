/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:SysLogFactory.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jan 16, 2013 11:33:02 AM
 * 
 */
package com.pixshow.framework.log.api;

import com.pixshow.framework.log.internal.SysLogImpl;
import com.pixshow.framework.utils.ClassUtility;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Jan 16, 2013
 * 
 */

public class SysLogFactory {
    public static SysLog getLog(Class<?> clazz) {
        return new SysLogImpl(clazz, null);
    }

    public static SysLog getLog(Object obj) {
        return new SysLogImpl(ClassUtility.getUserClass(obj), obj);
    }
}
