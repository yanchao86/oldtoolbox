/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:SchedulerManager.java Project: LvWeatherManager
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jun 25, 2013 6:59:26 PM
 * 
 */
package com.pixshow.framework.schedule.api;

import java.util.Map;

import org.quartz.Job;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Jun 25, 2013
 * 
 */

public interface SchedulerManager {

    public void deleteJob(String jobName);

    public void scheduleJob(String jobName, Class<? extends Job> jobClass, String cron, Map<String, Object> data);
}
