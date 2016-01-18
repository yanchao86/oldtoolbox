/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:GroupUtiltiyTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jan 6, 2013 7:23:34 PM
 * 
 */
package com.pixshow.framework.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.pixshow.framework.utils.LatLngUtility.LatLng;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Jan 6, 2013
 * 
 */

public class ListUtilityTests {

    public static class TestBean {
        private String id;
        private String name;
        private Date   date;
        private int    intType;
        private long   longType;

        public TestBean(String id, String name, Date date, int intType, long longType) {
            super();
            this.id = id;
            this.name = name;
            this.date = date;
            this.intType = intType;
            this.longType = longType;
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

        public int getIntType() {
            return intType;
        }

        public void setIntType(int intType) {
            this.intType = intType;
        }

        public long getLongType() {
            return longType;
        }

        public void setLongType(long longType) {
            this.longType = longType;
        }

    }

    private static List<TestBean> initData() {
        List<TestBean> list = new ArrayList<TestBean>();
        for (int i = 0; i < 1000; i++) {
            list.add(new TestBean("ID-" + i, "NAME-" + i, new Date(System.currentTimeMillis() + (i * 1000L)), 1, 1L));
        }

        for (int i = 0; i < 100; i++) {
            list.add(new TestBean("ID-" + i, "NAME-" + i, new Date(System.currentTimeMillis() + (i * 1000L)), 1, 1L));
        }
        return list;
    }

    public static void test_groupToList() {
        List<TestBean> data = initData();
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            ListUtiltiy.groupToList(data, "id");
            //            new SortingMap((ArrayList) data).doSort("getId");
        }
        long time2 = System.currentTimeMillis();
        System.out.println("list:" + (time2 - time1)); //list:10572 object:9659
    }

    public static void test_groupToObject() {
        List<TestBean> data = initData();
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            ListUtiltiy.groupToObject(data, "id");
        }
        long time2 = System.currentTimeMillis();

        System.out.println("list:" + (time2 - time1)); //list:10572 object:9659

    }

    public static void test_asc() {
        List<TestBean> list = new ArrayList<TestBean>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            list.add(new TestBean("ID-" + random.nextInt(1000), "NAME-" + random.nextInt(1000), new Date(random.nextLong()), random.nextInt(1000), random.nextLong()));
        }
        ListUtiltiy.asc(list, "id");
        println(list);
        ListUtiltiy.desc(list, "id");
        println(list);
        ListUtiltiy.desc(list, "date");
        println(list);
        ListUtiltiy.asc(list, "intType");
        println(list);
        ListUtiltiy.asc(list, "longType");
        println(list);
    }

    private static void println(List<TestBean> list) {
        System.out.println("------------------------------------------------------------");
        for (TestBean testBean : list) {
            System.out.println(ToStringBuilder.reflectionToString(testBean));
        }
        System.out.println("------------------------------------------------------------");
    }

    protected static void test_distanceSq() {
        System.out.println(LatLngUtility.distance(new LatLng(39.436193, 116.125488), new LatLng(39.446799, 116.250458)));
        System.out.println(D_jw(39.436193, 116.125488, 39.446799, 116.250458));

        System.out.println(LatLngUtility.distanceSq(new LatLng(34.420505, 115.109253), new LatLng(34.422771, 115.225983)));
        System.out.println(LatLngUtility.distanceSq(new LatLng(33.132951, 114.989777), new LatLng(33.136401, 115.117493)));
        System.out.println(LatLngUtility.distanceSq(new LatLng(33.142151, 115.236969), new LatLng(33.1479, 115.410004)));

        System.out.println();
    }

    public static double D_jw(double wd1, double jd1, double wd2, double jd2) {
        double x, y, out;
        double PI = 3.14159265;
        double R = 6.371229 * 1e6;

        x = (jd2 - jd1) * PI * R * Math.cos(((wd1 + wd2) / 2) * PI / 180) / 180;
        y = (wd2 - wd1) * PI * R / 180;
        out = Math.hypot(x, y);
        return out / 1000;
    }

    public static void main(String[] args) throws Exception {
        //        test_asc();
        test_distanceSq();
    }
}
