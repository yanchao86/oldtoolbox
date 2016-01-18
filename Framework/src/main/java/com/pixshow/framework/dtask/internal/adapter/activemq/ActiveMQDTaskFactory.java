package com.pixshow.framework.dtask.internal.adapter.activemq;

import com.pixshow.framework.config.Config;
import com.pixshow.framework.dtask.api.DTask;
import com.pixshow.framework.dtask.internal.AbstractSingleDTaskFactory;
import com.pixshow.framework.exception.api.SysException;
import com.pixshow.framework.utils.StringUtility;

public class ActiveMQDTaskFactory extends AbstractSingleDTaskFactory {

    @Override
    public DTask doInit() {

        String brokerURL = Config.getInstance().getString("dtask.client.activemq.brokerURL");
        int idleTimeout = Config.getInstance().getInteger("dtask.client.activemq.idleTimeout", -1);
        int maxConnections = Config.getInstance().getInteger("dtask.client.activemq.maxConnections", -1);

        if (StringUtility.isEmpty(brokerURL)) { throw new SysException("【任务分发起端】启动ActiveMQ分布式任务系统失败，未设置服务器地址!"); }

        ActiveMQDTask task = new ActiveMQDTask(brokerURL, idleTimeout, maxConnections);

        log.info("【任务分发起端】启动ActiveMQ分布式任务系统，连接到" + brokerURL);

        return task;
    }

    @Override
    public void doDestroy(DTask task) {
        ((ActiveMQDTask) task).stop();
    }
}
