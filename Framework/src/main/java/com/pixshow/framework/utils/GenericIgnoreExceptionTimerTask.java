/*
 * Copyright (c) 2010-2012 www.pixshow.net All Rights Reserved
 *
 * File:DefaultTimerTask.java Project: DTaskServer
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Dec 27, 2012 2:32:04 PM
 * 
 */
package com.pixshow.framework.utils;

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

public class GenericIgnoreExceptionTimerTask extends IgnoreExceptionTaskTimer {

    private Runnable runnable;

    public GenericIgnoreExceptionTimerTask(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void ignoreExceptionRun() {
        runnable.run();
    }

}
