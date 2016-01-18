/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:CahceUtility.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Mar 6, 2013 1:43:55 PM
 * 
 */
package com.pixshow.framework.cache.api;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Mar 6, 2013
 * 
 */

public class CacheUtility {

    public static interface ObjectGetter<T> {
        public T get();
    }

    public static interface ListGetter<T> {
        public T get(int index);
    }

    public static interface ListGetters<T> {
        public List<T> get(int index);
    }

    public static boolean isEmptyObject(Object obj) {
        if (obj instanceof EmptyObject) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null || obj instanceof EmptyObject) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        if (obj == null || obj instanceof EmptyObject) {
            return false;
        }
        return true;
    }

    public static <T> T getObjectAllowNull(String key, ObjectGetter<T> getter, Cache cache) {
        Object object = cache.get(key);
        if (object == null) {
            object = getter.get();
            if (object != null) {
                cache.set(key, object);
            }
        }
        return isEmpty(object) ? null : (T) object;
    }

    /**
     * 慎用!!,后果自负
     * 
     * @param <T>
     * @param key
     * @param getter
     * @param cache
     * @return
     *
     */
    public static <T> List<T> getListAllowNull(List<String> keys, ListGetter<T> getter, Cache cache) {
        List<T> objectList = new ArrayList<T>();
        List<Object> cachedObjectList = cache.get(keys);
        for (int index = 0; index < cachedObjectList.size(); index++) {
            Object object = cachedObjectList.get(index);
            if (object == null) {
                object = getter.get(index);
                if (object != null) {
                    cache.set(keys.get(index), object);
                }
            }
            objectList.add(isEmpty(object) ? null : (T) object);
        }
        return objectList;
    }

    public static <T> T getObject(String key, ObjectGetter<T> getter, Cache cache) {
        Object object = cache.get(key);
        if (object == null) {
            object = getter.get();
            if (object == null) {
                cache.set(key, new EmptyObject());
            } else {
                cache.set(key, object);
            }
        }
        return isEmpty(object) ? null : (T) object;
    }

    public static <T> List<T> getList(List<String> keys, ListGetter<T> getter, Cache cache) {
        List<T> objectList = new ArrayList<T>();
        List<Object> cachedObjectList = cache.get(keys);
        for (int index = 0; index < cachedObjectList.size(); index++) {
            Object object = cachedObjectList.get(index);
            if (object == null) {
                object = getter.get(index);
                if (object == null) {
                    cache.set(keys.get(index), new EmptyObject());
                } else {
                    cache.set(keys.get(index), object);
                }
            }
            objectList.add(isEmpty(object) ? null : (T) object);
        }
        return objectList;
    }
}
