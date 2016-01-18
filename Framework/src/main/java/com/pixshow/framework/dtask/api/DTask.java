package com.pixshow.framework.dtask.api;

public interface DTask {

    public String createJob(String jobName, String data);

    public String createJob(String jobName, String data, String groupId);
}
