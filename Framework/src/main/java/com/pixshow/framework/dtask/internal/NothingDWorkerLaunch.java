package com.pixshow.framework.dtask.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.pixshow.framework.dtask.api.DWorkerLaunch;

public class NothingDWorkerLaunch implements DWorkerLaunch {

    private Log log = LogFactory.getLog(getClass());

    @Override
    public void init(ApplicationContext applicationContext) {
        log.info("【任务执行端】未启用任何分布式任务系统 !");
    }

    @Override
    public void shutdown() {
    }

}
