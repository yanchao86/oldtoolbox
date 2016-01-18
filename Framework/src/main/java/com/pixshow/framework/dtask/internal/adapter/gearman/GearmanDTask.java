package com.pixshow.framework.dtask.internal.adapter.gearman;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gearman.client.GearmanClient;
import org.gearman.client.GearmanClientImpl;
import org.gearman.client.GearmanJob;
import org.gearman.client.GearmanJobImpl;
import org.gearman.common.GearmanNIOJobServerConnection;

import com.pixshow.framework.dtask.api.DTask;

public class GearmanDTask implements DTask {

    private Log           log    = LogFactory.getLog(getClass());
    private GearmanClient client = null;

    public GearmanDTask(String server) {
        try {
            String[] ip = server.split(":");
            client = new GearmanClientImpl();
            if (ip.length > 1) {
                client.addJobServer(new GearmanNIOJobServerConnection(ip[0], Integer.parseInt(ip[1])));
            } else {
                client.addJobServer(new GearmanNIOJobServerConnection(ip[0]));
            }
        } catch (Exception e) {
            client = null;
            log.error(e.getMessage(), e);
        }
    }

    public void shutdown() {
        if (client != null) {
            client.shutdown();
        }
    }

    @Override
    public String createJob(String jobName, String data) {
        byte[] bytes;
        try {
            bytes = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            bytes = data.getBytes();
            log.warn(e.getMessage(), e);
        }
        String id = UUID.randomUUID().toString();
        GearmanJob job = GearmanJobImpl.createBackgroundJob(jobName, bytes, id);
        client.submit(job);
        if (log.isInfoEnabled()) {
            log.info("job submit >>>>>>>>>> " + jobName);
        }
        return id;
    }

    @Override
    public String createJob(String jobName, String data, String groupId) {
        return createJob(jobName, data);
    }

}
