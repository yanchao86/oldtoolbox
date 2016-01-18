/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:SysLog.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jan 16, 2013 10:59:12 AM
 * 
 */
package com.pixshow.framework.log.api;

import java.util.Map;

import org.apache.commons.logging.Log;

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

public interface SysLog extends Log {

    public void log(String type, String message);

    public void log(String type, String message, Throwable throwable);

    public void log(String type, String message, Map<String, Object> info);

    public void log(String type, String message, Map<String, Object> info, Throwable throwable);
}
