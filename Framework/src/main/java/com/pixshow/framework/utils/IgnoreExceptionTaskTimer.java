/*
 * Copyright (c) 2010-2012 www.pixshow.net All Rights Reserved
 *
 * File:IgnoreETaskTimer.java Project: DTaskClient
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Dec 26, 2012 5:59:38 PM
 * 
 */
package com.pixshow.framework.utils;

import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Dec 26, 2012
 * 
 */

public abstract class IgnoreExceptionTaskTimer extends TimerTask {

    private Log log = LogFactory.getLog(getClass());

    @Override
    public void run() {
        try {
            ignoreExceptionRun();
        } catch (Exception e) {
            exception(e);
        }
    }

    abstract public void ignoreExceptionRun();

    public void exception(Exception e) {
        log.warn(e.getMessage(), e);
    }

}
