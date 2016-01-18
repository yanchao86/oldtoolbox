package com.pixshow.framework.dtask.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pixshow.framework.dtask.api.DTask;
import com.pixshow.framework.dtask.api.DTaskFactory;

public abstract class AbstractSingleDTaskFactory implements DTaskFactory {

    protected Log log  = LogFactory.getLog(getClass());

    private DTask task = null;

    @Override
    public DTask createDTask() {
        if (task == null) {
            init();
        }
        return task;
    }

    @Override
    public synchronized void destroy() {
        if (task != null) {
            doDestroy(task);
            task = null;
        }
    }

    private synchronized void init() {
        if (task == null) {
            task = doInit();
        }
    }

    public abstract DTask doInit();

    public abstract void doDestroy(DTask task);

}
