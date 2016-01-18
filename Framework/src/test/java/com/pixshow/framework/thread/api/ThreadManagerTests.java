/*
 * Copyright (c) 2010-2012 www.pixshow.net All Rights Reserved
 *
 * File:ThreadManagerTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Nov 8, 2012 5:26:35 PM
 * 
 */
package com.pixshow.framework.thread.api;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$
 * @since Nov 8, 2012
 * 
 */

public class ThreadManagerTests {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            final String name = "name-" + i;
            ThreadManager.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("!!!!!!!!!111111Begin " + name + " !!!!!!!!!");
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                    }
                    System.out.println("!!!!!!!!111111End!!!!!!!!!!" + name);
                }
            });
        }
//        ThreadManager.getInstance().shutdown();
        System.out.println("11111111111111任务添加完成");

        for (int i = 0; i < 10; i++) {
            final String name = "name-" + i;
            ThreadManager.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("!!!!!!!!!222222Begin " + name + " !!!!!!!!!");
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                    }
                    System.out.println("!!!!!!!!2222222End!!!!!!!!!!" + name);
                }
            });
        }
//        ThreadManager.getInstance().shutdown();
        System.out.println("22222222222任务添加完成");
    }
}
