/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:SysLog.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jan 16, 2013 12:07:48 PM
 * 
 */
package com.pixshow.framework.log;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.log4j.LogManager;

import com.pixshow.framework.log.api.SysLog;
import com.pixshow.framework.log.api.SysLogFactory;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Jan 16, 2013
 * 
 */

public class SysLogTest {

    private static SysLog log = SysLogFactory.getLog(SysLogTest.class);

    public static void test1() {
        for (int i = 0; i < 1000; i++) {
            Map<String, Object> info = new HashMap<String, Object>();
            info.put("key1", UUID.randomUUID().toString());
            info.put("key2", UUID.randomUUID().toString());
            info.put("key3", UUID.randomUUID().toString());
            long time1 = System.currentTimeMillis();
            log.log("test", "asdasd", info);
            long time2 = System.currentTimeMillis();
            System.out.println(time2 - time1);
        }
    }

    public static void test2() {
        Map<String, Object> info = new HashMap<String, Object>();
        info.put("key1", UUID.randomUUID().toString());
        info.put("key2", UUID.randomUUID().toString());
        info.put("key3", UUID.randomUUID().toString());
        long time1 = System.currentTimeMillis();
        log.log("test", "asdasd", info, new RuntimeException("~~~~~~~~~~~~~~~~~~~~~"));
        log.log("test", "asdasd", info, new RuntimeException("~~~~~~~~~~~~~~~~~~~~~"));
        long time2 = System.currentTimeMillis();
        System.out.println(time2 - time1);
    }

    public static void test3() {
        try {
            java.util.logging.LogManager.getLogManager().readConfiguration(SysLogTest.class.getResourceAsStream("/logging.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger logger = Logger.getLogger("xxxxx");
        logger.warning("logger.warning");
        logger.log(Level.SEVERE, "logger.log(Level.SEVERE)");
        logger.log(Level.WARNING, "logger.log(Level.WARNING)");
    }

    public static void main(String[] args) {

        //        test2();
        test3();

        LogManager.shutdown();
    }
}
