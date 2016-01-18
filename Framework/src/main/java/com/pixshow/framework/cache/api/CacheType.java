/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:CacheType.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:May 18, 2012 2:12:15 PM
 * 
 */
package com.pixshow.framework.cache.api;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.2 $ $Date: 2012/07/12 09:33:55 $
 * @since May 18, 2012
 * 
 */

public enum CacheType {
    /**
     * 全局缓存
     */
    Global("global"),
    /**
     * 全局高速
     */
    GlobalHigh("global.high"),
    /**
     * 应用缓存
     */
    App("app"),
    /**
     * 应用高速缓存
     */
    AppHigh("app.high");

    private String key;

    /**
     * @param key
     */
    private CacheType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
