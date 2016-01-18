/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:ProxyTransactionCache.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Mar 12, 2013 2:55:49 PM
 * 
 */
package com.pixshow.framework.cache.internal.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pixshow.framework.cache.api.Cache;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Mar 12, 2013
 * 
 */

public class ProxyTransactionCache implements Cache {

    private Cache                target;

    private Map<String, Object>  setTemp    = new HashMap<String, Object>();
    private Map<String, Integer> expireTemp = new HashMap<String, Integer>();
    private List<String>         deleteTemp = new ArrayList<String>();

    public ProxyTransactionCache(Cache target) {
        this.target = target;
    }

    @Override
    public Object delete(String key) {
        deleteTemp.add(key);
        return target.get(key);
    }

    @Override
    public void set(String key, Object value) {
        setTemp.put(key, value);
    }

    @Override
    public void set(String key, Object value, int expire) {
        setTemp.put(key, value);
        expireTemp.put(key, expire);
    }

    @Override
    public Object get(String key) {
        return target.get(key);
    }

    @Override
    public List<Object> get(List<String> key) {
        return target.get(key);
    }

    @Override
    public boolean keyExists(String key) {
        return target.keyExists(key);
    }

    public void doCommit() {
        for (String key : deleteTemp) {
            target.delete(key);
        }
        for (String key : setTemp.keySet()) {
            Integer expire = expireTemp.get(key);
            if (expire == null) {
                target.set(key, target.get(key));
            } else {
                target.set(key, target.get(key), expire);
            }
        }
        clear();
    }

    public void doRollback() {
        clear();
    }

    private void clear() {
        deleteTemp.clear();
        setTemp.clear();
        expireTemp.clear();
    }

}
