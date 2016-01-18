/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:Worker.java Project: LvWeatherService
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Oct 22, 2013 3:12:39 PM
 * 
 */
package com.pixshow.framework.dtask.api;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

import com.pixshow.framework.config.Config;
import com.pixshow.framework.dtask.internal.NothingDWorkerLaunch;
import com.pixshow.framework.dtask.internal.adapter.activemq.ActiveMQDWorkerLaunch;
import com.pixshow.framework.dtask.internal.adapter.gearman.GearmanDWorkerLaunch;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Oct 22, 2013
 * 
 */

@Component
public class DWorkerLaunchManager implements ApplicationListener<ApplicationEvent>, ApplicationContextAware {

    // private Log log = LogFactory.getLog(getClass());

    private ApplicationContext applicationContext;
    private DWorkerLaunch      worker = null;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextClosedEvent || event instanceof ContextStoppedEvent) {
            shutdown();
        } else if (event instanceof ContextStartedEvent || event instanceof ContextRefreshedEvent) {
            init();
        }
    }

    public synchronized void shutdown() {
        if (worker != null) {
            worker.shutdown();
            worker = null;
        }
    }

    public synchronized void init() {
        if (worker == null) {
            String adapter = Config.getInstance().getString("dtask.adapter");
            if ("gearman".equalsIgnoreCase(adapter)) {
                worker = new GearmanDWorkerLaunch();
            } else if ("activemq".equalsIgnoreCase(adapter)) {
                worker = new ActiveMQDWorkerLaunch();
            } else {
                worker = new NothingDWorkerLaunch();
            }
            worker.init(applicationContext);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
