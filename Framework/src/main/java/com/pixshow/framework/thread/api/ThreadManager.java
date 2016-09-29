/*
 * Copyright (c) 2010-2012 www.pixshow.net All Rights Reserved
 *
 * File:TheadManager.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Nov 8, 2012 5:10:03 PM
 * 
 */
package com.pixshow.framework.thread.api;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.pixshow.framework.config.Config;

public class ThreadManager {

    private ThreadPoolExecutor executor;

    private static final ThreadManager instance = new ThreadManager();

    private ThreadManager() {
        int corePoolSize = Config.getInstance().getInteger("threadPool.corePoolSize", 10);
        int maximumPoolSize = Config.getInstance().getInteger("threadPool.maximumPoolSize", 50);
        int keepAliveTime = Config.getInstance().getInteger("threadPool.keepAliveTime", 1000);
        
        executor = new ThreadPoolExecutor(//
                corePoolSize, //
                maximumPoolSize, //
                keepAliveTime, //
                TimeUnit.SECONDS, //
                new ArrayBlockingQueue<Runnable>(corePoolSize), //
                new ThreadPoolExecutor.CallerRunsPolicy()//
        );
    }

    public static ThreadManager getInstance() {
        return instance;
    }

    public void execute(Runnable command) {
        executor.execute(command);
    }

    public void shutdown() {
        executor.shutdown();
    }
}
