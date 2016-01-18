/*
 * Copyright (c) 2010-2012 www.pixshow.net All Rights Reserved
 *
 * File:RedisCacheTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Nov 8, 2012 5:04:50 PM
 * 
 */
package com.pixshow.framework.cache.internal.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pixshow.framework.cache.api.CacheUtility;
import com.pixshow.framework.cache.api.EmptyObject;
import com.pixshow.framework.utils.DateUtility;

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

public class RedisCacheTests {

    private static void testSetGet() {
        RedisCache cache = new RedisCache();
        cache.set("key1", DateUtility.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        System.out.println(cache.get("key1"));
    }

    private static void testSetList() {
        RedisCache cache = new RedisCache();

        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            String k = "Key-" + i;
            cache.set(k, new Date());
        }
        long time2 = System.currentTimeMillis();
        System.out.println("testSet -> " + (time2 - time1));
    }

    private static void testGetList() {
        RedisCache cache = new RedisCache();

        List<String> keys = new ArrayList<String>();
        for (int i = 0; i < 1000; i++) {
            keys.add("Key-" + i);
        }

        long time1 = System.currentTimeMillis();
        List<Object> data = cache.get(keys);
        long time2 = System.currentTimeMillis();

        System.out.println("testGetList -> " + (time2 - time1) + " size=" + data.size());
    }

    private static void testEmptyObject() {
        RedisCache cache = new RedisCache();
        cache.set("key1", new EmptyObject());
        Object obj1 = cache.get("key1");
        Object obj2 = cache.get("key2");
        System.out.println(obj1 + " " + CacheUtility.isEmpty(obj1));
        System.out.println(obj2 + " " + CacheUtility.isEmpty(obj2));
    }

    public static void main(String[] args) {
        testSetGet();
        //        testSetList();
        //        testEmptyObject();
        //        testGetList();
        //        RedisCache cache = new RedisCache();
        //        cache.delete("memberRouting@1");
        //        System.out.println(cache.get("memberRouting@1"));
    }
}
