/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:EmptyJob.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Nov 19, 2013 11:26:24 AM
 * 
 */
package com.pixshow.framework.schedule.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Nov 19, 2013
 * 
 */

public class EmptyWrapJob implements Job {

    private Class<? extends Job> original = null;
    private Log                  log      = null;

    public EmptyWrapJob(Class<? extends Job> original) {
        this.original = original;
        this.log = LogFactory.getLog(original);
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Job [" + original.getName() + "] Empty run..");
    }
}
