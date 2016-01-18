/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:SchedulerDebugConfig.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Nov 19, 2013 11:07:27 AM
 * 
 */
package com.pixshow.framework.schedule.api;

import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;

import com.pixshow.framework.schedule.internal.EmptyWrapJob;

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
public abstract class SchedulerTestConfig {

    private List<Class<? extends Job>> jobClasses = new ArrayList<Class<? extends Job>>();

    public abstract void runJobClass();

    protected void registerRunJobClass(Class<? extends Job> jobClass) {
        jobClasses.add(jobClass);
    }

    public boolean isRunJobClass(Class<? extends Job> original) {
        return jobClasses.contains(original);
    }

    public Job getWrapJob(Class<? extends Job> original) {
        return new EmptyWrapJob(original);
    }

}
