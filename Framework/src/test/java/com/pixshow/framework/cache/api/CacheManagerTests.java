/*
 * Copyright (c) 2010-2012 www.pixshow.net All Rights Reserved
 *
 * File:CacheManagerTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Nov 8, 2012 5:08:04 PM
 * 
 */
package com.pixshow.framework.cache.api;

import java.util.Date;

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

public class CacheManagerTests {
    public static void main(String[] args) {
        Cache cache = CacheManager.getInstance().getCache();
        
        cache.delete("");

        String key = "Key-1";

        System.out.println(cache.get(key));
        cache.set(key, new Date());
        System.out.println(cache.get(key));
        cache.delete(key);
        System.out.println(cache.get(key));
    }
}
