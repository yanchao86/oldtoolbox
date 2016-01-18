package com.pixshow.framework.dtask.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pixshow.framework.dtask.api.DTask;
import com.pixshow.framework.dtask.api.DTaskFactory;

public class NothingDTaskFactory implements DTaskFactory {

    private Log          log  = LogFactory.getLog(getClass());
    private NothingDTask task = new NothingDTask();

    @Override
    public DTask createDTask() {
        log.info("【任务分发起端】未启用任何分布式任务系统 !");
        return task;
    }

    @Override
    public void destroy() {
    }

}
