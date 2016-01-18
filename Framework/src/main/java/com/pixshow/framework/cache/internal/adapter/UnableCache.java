/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:UnableCache.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:May 18, 2012 3:45:55 PM
 * 
 */
package com.pixshow.framework.cache.internal.adapter;

import java.util.ArrayList;
import java.util.List;

import com.pixshow.framework.cache.api.Cache;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.2 $ $Date: 2012/07/12 09:33:54 $
 * @since May 18, 2012
 * 
 */

public class UnableCache implements Cache {

    @Override
    public Object delete(String key) {
        return null;
    }

    @Override
    public Object get(String key) {
        return null;
    }

    @Override
    public boolean keyExists(String key) {
        return false;
    }

    @Override
    public void set(String key, Object value) {
    }

    @Override
    public void set(String key, Object value, int expire) {
    }

    @Override
    public List<Object> get(List<String> keys) {
        List<Object> list = new ArrayList<Object>();
        for (String key : keys) {
            list.add(null);
        }
        return list;
    }

}
