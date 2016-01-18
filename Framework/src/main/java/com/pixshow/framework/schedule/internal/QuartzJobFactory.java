/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:SpringQuartzJobFactory.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jul 2, 2012 2:54:07 PM
 * 
 */
package com.pixshow.framework.schedule.internal;

import org.quartz.Job;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

import com.pixshow.framework.schedule.api.SchedulerTestConfig;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.2 $ $Date: 2012/07/12 09:33:56 $
 * @since Jul 2, 2012
 * 
 */

@Component("framework.schedule.QuartzJobFactory")
public class QuartzJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {

    private ApplicationContext  applicationContext;
    private SchedulerTestConfig testConfig;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Job job = null;
        Class<? extends Job> jobClass = bundle.getJobDetail().getJobClass();

        if (testConfig != null && !testConfig.isRunJobClass(jobClass)) {
            job = testConfig.getWrapJob(jobClass);
        }
        try {
            if (job == null) {
                job = (Job) applicationContext.getBean(jobClass);
            }
        } catch (Exception e) {
            job = jobClass.newInstance();
        }
        return job;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

        try {
            this.testConfig = applicationContext.getBean(SchedulerTestConfig.class);
        } catch (Throwable e) {
        }

    }
}
