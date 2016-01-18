/*
 * Copyright (c) 2010-2012 www.pixshow.net All Rights Reserved
 *
 * File:XBeanUtility.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Dec 12, 2012 5:52:40 PM
 * 
 */
package com.pixshow.framework.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.jxpath.JXPathContext;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Dec 12, 2012
 * 
 */

public class XPathUtility {

    @SuppressWarnings("unchecked")
    public static <T> List<T> getList(Object obj, String xpath) {
        JXPathContext context = JXPathContext.newContext(obj);
        Iterator it = context.iterate(xpath);
        List list = new ArrayList();
        while (it.hasNext()) {
            Object n = it.next();
            if (list.contains(n)) {
                continue;
            }
            list.add(n);
        }
        return list;
    }

    public static <T> String getString(Object obj, String xpath) {
        List<T> list = getList(obj, xpath);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            builder.append(list.get(i));
        }
        return builder.toString();
    }

    /**
     * 没有去除重复的
     * @param obj
     * @param sep
     * @param xpaths
     * @return
     *
     */
    public static List<CompositeKey> getCompositeKey(Object obj, CompositeKey.Separator sep, String... xpaths) {
        JXPathContext context = JXPathContext.newContext(obj);
        List<CompositeKey> keys = new ArrayList<CompositeKey>();
        for (String xpath : xpaths) {
            Iterator<?> it = context.iterate(xpath);
            for (int index = 0; it.hasNext(); index++) {
                Object key = it.next();
                CompositeKey compositeKey = null;
                if (keys.size() > index) {
                    compositeKey = keys.get(index);
                }
                if (compositeKey == null) {
                    if (sep == null) {
                        compositeKey = new CompositeKey(key);
                    } else {
                        compositeKey = new CompositeKey(sep, key);
                    }
                    keys.add(compositeKey);
                } else {
                    compositeKey.addKey(key);
                }
            }
        }
        return keys;
    }

    public static List<CompositeKey> getCompositeKey(Object obj, String... xpaths) {
        return getCompositeKey(obj, null, xpaths);
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<T> getSet(Object obj, String xpath) {
        JXPathContext context = JXPathContext.newContext(obj);
        Iterator it = context.iterate(xpath);
        Set set = new LinkedHashSet();
        while (it.hasNext()) {
            set.add(it.next());
        }
        return set;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getObject(Object obj, String xpath) {
        JXPathContext context = JXPathContext.newContext(obj);
        return (T) context.getValue(xpath);
    }

    //    public static <K, V> Map<K, V> getMap() {
    //        return null;
    //    }

}