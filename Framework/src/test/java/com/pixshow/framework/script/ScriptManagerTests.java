/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:ScriptManagerTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jun 6, 2013 3:38:37 PM
 * 
 */
package com.pixshow.framework.script;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pixshow.framework.script.api.ScriptManager;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Jun 6, 2013
 * 
 */

public class ScriptManagerTests {

    protected ClassPathXmlApplicationContext context     = null;
    protected TestService                    testService = null;

    public ScriptManagerTests() {
        context = new ClassPathXmlApplicationContext("classpath:/config/framework/spring.xml");
        testService = context.getBean(TestService.class);
    }

    public void test_1() {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("testService", testService);
        context.put("p1", "1");
        context.put("p2", "2");
        context.put("p3", "3");
        context.put("p4", "4");
        ScriptManager.exec("test2.py", context);
    }

    public static void main(String[] args) {
        ScriptManagerTests tests = new ScriptManagerTests();
        tests.test_1();
    }
}
