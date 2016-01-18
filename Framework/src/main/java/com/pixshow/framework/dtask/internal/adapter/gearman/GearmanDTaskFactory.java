package com.pixshow.framework.dtask.internal.adapter.gearman;

import com.pixshow.framework.config.Config;
import com.pixshow.framework.dtask.api.DTask;
import com.pixshow.framework.dtask.internal.AbstractSingleDTaskFactory;
import com.pixshow.framework.exception.api.SysException;
import com.pixshow.framework.utils.StringUtility;

public class GearmanDTaskFactory extends AbstractSingleDTaskFactory {

    public DTask doInit() {
        String server = Config.getInstance().getString("dtask.client.gearman.address");

        if (StringUtility.isEmpty(server)) { throw new SysException("【任务分发起端】启动Gearman分布式任务系统失败，未设置服务器地址!"); }

        GearmanDTask task = new GearmanDTask(server);

        log.info("【任务分发起端】启动Gearman分布式任务系统，连接到" + server);

        return task;
    }

    public void doDestroy(DTask task) {
        ((GearmanDTask) task).shutdown();
    }

}
