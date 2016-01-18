/*
 * Copyright (c) 2010-2012 www.pixshow.net All Rights Reserved
 *
 * File:XPathUtilityTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Dec 13, 2012 11:00:13 AM
 * 
 */
package com.pixshow.framework.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Dec 13, 2012
 * 
 */

public class XPathUtilityTests {
    public static class TestBean {
        private String id;
        private String name;
        private Date   date;

        private TestBean(String id, String name, Date date) {
            this.id = id;
            this.name = name;
            this.date = date;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }

    private List<TestBean> initData() {
        List<TestBean> list = new ArrayList<TestBean>();
        for (int i = 0; i < 10; i++) {
            list.add(new TestBean("ID-" + i, "NAME-" + i, new Date(System.currentTimeMillis() + (i * 1000L))));
        }
        list.add(new TestBean("ID-" + 1, "NAME-" + 1, new Date(System.currentTimeMillis() + (1 * 1000L))));
        return list;
    }

    @Test
    public void test_getList() {
        System.out.println(XPathUtility.getList(initData(), "id"));
    }

    @Test
    public void test_getSet() {
        System.out.println(XPathUtility.getSet(initData(), "id"));
    }

    @Test
    public void test_getObject() {
        System.out.println(XPathUtility.getObject(initData(), "id[1]"));
    }
}
