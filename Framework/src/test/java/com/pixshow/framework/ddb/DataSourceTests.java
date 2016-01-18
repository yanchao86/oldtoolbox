/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:PoolTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Mar 26, 2013 9:27:50 PM
 * 
 */
package com.pixshow.framework.ddb;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pixshow.framework.support.BaseDao;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Mar 26, 2013
 * 
 */

public class DataSourceTests extends BaseDao {
    
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:/config/framework/spring.xml");
        Test1Service service1 = context.getBean(Test1Service.class);
        Test2Service service2 = context.getBean(Test2Service.class);
        //        service1.save();
        while (true) {
            try {
                service2.query();
            } catch (Exception e) {
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        }
    }
}
