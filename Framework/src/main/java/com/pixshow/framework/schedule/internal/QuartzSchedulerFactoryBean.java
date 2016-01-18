/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:ExtSchedulerFactoryBean.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jul 2, 2012 2:48:37 PM
 * 
 */
package com.pixshow.framework.schedule.internal;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;

import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

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

@Component("framework.schedule.QuartzSchedulerFactoryBean")
public class QuartzSchedulerFactoryBean extends SchedulerFactoryBean {

    public static final String CONFIG_FILE   = "/config/config_schedule.properties";
    private String             schedulerName = null;

    @Resource(name = "framework.schedule.QuartzJobFactory")
    @Override
    public void setJobFactory(JobFactory jobFactory) {
        super.setJobFactory(jobFactory);
    }

    public QuartzSchedulerFactoryBean() {
        super();
        ClassPathResource config = new ClassPathResource(CONFIG_FILE);
        if (config.exists()) {
            Properties properties = null;
            try {
                properties = PropertiesLoaderUtils.loadProperties(config);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (properties != null) {
                this.schedulerName = properties.getProperty(StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME);
                setSchedulerName(this.schedulerName);

                setConfigLocation(new ClassPathResource(CONFIG_FILE));
                setWaitForJobsToCompleteOnShutdown(true);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.schedulerName != null) {
            super.afterPropertiesSet();
        }
    }

}
