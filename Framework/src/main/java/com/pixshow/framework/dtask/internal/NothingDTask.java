package com.pixshow.framework.dtask.internal;

import com.pixshow.framework.dtask.api.DTask;

public class NothingDTask implements DTask {

    @Override
    public String createJob(String jobName, String data) {
        return null;
    }

    @Override
    public String createJob(String jobName, String data, String groupId) {
        return null;
    }

}
