package com.pixshow.framework.dtask.internal.adapter.gearman;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gearman.common.GearmanNIOJobServerConnection;
import org.gearman.worker.GearmanWorker;
import org.gearman.worker.GearmanWorkerImpl;
import org.springframework.context.ApplicationContext;

import com.pixshow.framework.config.Config;
import com.pixshow.framework.dtask.api.DWorker;
import com.pixshow.framework.dtask.api.DWorkerLaunch;
import com.pixshow.framework.plugin.api.PluginDefinition;
import com.pixshow.framework.plugin.api.PluginRegisterManager;
import com.pixshow.framework.utils.StringUtility;

public class GearmanDWorkerLaunch implements DWorkerLaunch, Runnable {
    private Log                log = LogFactory.getLog(getClass());

    private GearmanWorker      worker;
    private ApplicationContext applicationContext;

    @Override
    public void init(ApplicationContext applicationContext) {

        this.applicationContext = applicationContext;

        if (worker == null) {
            new Thread(this).start();
        }
    }

    @Override
    public void shutdown() {
        if (worker != null) {
            try {
                worker.shutdown();
            } catch (Exception e) {
            }
            worker = null;
        }
    }

    @Override
    public void run() {
        String server = Config.getInstance().getString("dtask.server.gearman.address"); // 192.168.1.211:4730
        int maxPoolSize = Config.getInstance().getInteger("dtask.server.gearman.maxPoolSize", 50);

        if (StringUtility.isEmpty(server)) {
            log.warn("【任务执行端】启动Gearman分布式任务系统失败，未设置服务器地址!");
            return;
        }

        try {
            String[] ip = server.split(":");
            ThreadPoolExecutor executor = new ThreadPoolExecutor(maxPoolSize, maxPoolSize * 2, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(maxPoolSize), new ThreadPoolExecutor.CallerRunsPolicy());
            worker = new GearmanWorkerImpl(executor);
            if (ip.length > 1) {
                worker.addServer(new GearmanNIOJobServerConnection(ip[0], Integer.parseInt(ip[1])));
            } else {
                worker.addServer(new GearmanNIOJobServerConnection(ip[0]));
            }

            List<PluginDefinition> defs = PluginRegisterManager.getInstance().getDefinitions(DWorker.class);
            for (PluginDefinition def : defs) {
                worker.registerFunctionFactory(new ExecuterGearmanFunctionFactory(def.getAnnotation().name(), def.getBeanName(), applicationContext));
            }

            log.info("【任务执行端】启动Gearman分布式任务系统 ! 连接到" + server + " 线程池:" + maxPoolSize);
            worker.work();
        } catch (Exception e) {
            shutdown();
        }
    }
}
