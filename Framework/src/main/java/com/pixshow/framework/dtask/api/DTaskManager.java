/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:ClientReceive.java Project: LvWeatherPushService
 * 
 * Creator:4399-lvtu-8 
 * Date:Nov 5, 2013 5:35:30 PM
 * 
 */
package com.pixshow.framework.dtask.api;

import com.pixshow.framework.config.Config;
import com.pixshow.framework.dtask.internal.NothingDTaskFactory;
import com.pixshow.framework.dtask.internal.adapter.activemq.ActiveMQDTaskFactory;
import com.pixshow.framework.dtask.internal.adapter.gearman.GearmanDTaskFactory;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Nov 5, 2013
 * 
 */

public class DTaskManager {

    // private Log log = LogFactory.getLog(getClass());

    private static DTaskFactory factory = null;

    public static DTask getInstance() {
        if (factory == null) {
            init();
        }
        return factory.createDTask();
    }

    private synchronized static void init() {
        if (factory == null) {
            String adapter = Config.getInstance().getString("dtask.adapter");
            if ("gearman".equalsIgnoreCase(adapter)) {
                factory = new GearmanDTaskFactory();
            } else if ("activemq".equalsIgnoreCase(adapter)) {
                factory = new ActiveMQDTaskFactory();
            } else {
                factory = new NothingDTaskFactory();
            }
        }
    }

    public static void shutdown() {
        if (factory != null) factory.destroy();
    }

}
