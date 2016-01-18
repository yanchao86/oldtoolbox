package com.pixshow.framework.dtask.api;

import org.springframework.context.ApplicationContext;

public interface DWorkerLaunch {

    public void init(ApplicationContext applicationContext);

    public void shutdown();
}
