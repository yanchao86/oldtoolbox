/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:FunctionFactory.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Dec 27, 2013 12:22:20 PM
 * 
 */
package com.pixshow.framework.dtask.internal.adapter.gearman;

import org.gearman.worker.GearmanFunction;
import org.gearman.worker.GearmanFunctionFactory;
import org.springframework.context.ApplicationContext;

import com.pixshow.framework.dtask.api.DWorker;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Dec 27, 2013
 * 
 */

public class ExecuterGearmanFunctionFactory implements GearmanFunctionFactory {

    private String             workName;
    private String             beanName;
    private ApplicationContext applicationContext;

    public ExecuterGearmanFunctionFactory(String workName, String beanName, ApplicationContext applicationContext) {
        this.workName = workName;
        this.beanName = beanName;
        this.applicationContext = applicationContext;
    }

    @Override
    public GearmanFunction getFunction() {
        DWorker worker = (DWorker) applicationContext.getBean(beanName);
        return new ExecuterGearmanFunction(worker);
    }

    @Override
    public String getFunctionName() {
        return this.workName;
    }
}
