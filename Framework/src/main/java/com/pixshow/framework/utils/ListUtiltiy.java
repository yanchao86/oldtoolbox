/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:GroupUtiltiy.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jan 6, 2013 7:13:35 PM
 * 
 */
package com.pixshow.framework.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.ListUtils;

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

public class ListUtiltiy extends ListUtils {

    @SuppressWarnings("unchecked")
    public static <K, T> Map<K, List<T>> groupToList(List<T> list, String name) {
        Map<K, List<T>> map = new HashMap<K, List<T>>();
        for (T value : list) {
            K key = (K) BeanUtility.getProperty(value, name);
            List<T> values = map.get(key);
            if (values == null) {
                values = new ArrayList<T>();
                map.put(key, values);
            }
            values.add(value);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static <T> Map<CompositeKey, List<T>> groupToList(List<T> list, String... name) {
        Map<CompositeKey, List<T>> map = new HashMap<CompositeKey, List<T>>();
        for (T value : list) {
            CompositeKey key = null;
            for (String pro : name) {
                if (key == null) {
                    key = new CompositeKey(BeanUtility.getProperty(value, pro));
                } else {
                    key.addKey(BeanUtility.getProperty(value, pro));
                }
                if (key == null) {
                    break;
                }
                List<T> values = map.get(key);
                if (values == null) {
                    values = new ArrayList<T>();
                    map.put(key, values);
                }
                values.add(value);
            }
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static <K, T> Map<K, T> groupToObject(List<T> list, String name) {
        Map<K, T> map = new HashMap<K, T>();
        for (T value : list) {
            K key = (K) BeanUtility.getProperty(value, name);
            map.put(key, value);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static <T> Map<CompositeKey, T> groupToObject(List<T> list, String... name) {
        Map<CompositeKey, T> map = new HashMap<CompositeKey, T>();
        for (T value : list) {
            CompositeKey key = null;
            for (String pro : name) {
                if (key == null) {
                    key = new CompositeKey(BeanUtility.getProperty(value, pro));
                } else {
                    key.addKey(BeanUtility.getProperty(value, pro));
                }

                if (key == null) {
                    break;
                }
                map.put(key, value);
            }
        }
        return map;
    }

    public static <T> void asc(List<T> list, final String name) {
        Collections.sort(list, new Comparator<T>() {
            @SuppressWarnings("unchecked")
            @Override
            public int compare(T o1, T o2) {
                Object v1 = BeanUtility.getProperty(o1, name);
                Object v2 = BeanUtility.getProperty(o2, name);
                if (v1 instanceof Comparable) { return ((Comparable) v1).compareTo(v2); }
                return 0;
            }
        });
    }

    public static <T> void desc(List<T> list, final String name) {
        Collections.sort(list, new Comparator<T>() {
            @SuppressWarnings("unchecked")
            @Override
            public int compare(T o1, T o2) {
                Object v1 = BeanUtility.getProperty(o2, name);
                Object v2 = BeanUtility.getProperty(o1, name);
                if (v1 instanceof Comparable) { return ((Comparable) v1).compareTo(v2); }
                return 0;
            }
        });
    }

    public static <K, T> List<T> toList(Map<K, T> map) {
        List<T> list = new ArrayList<T>();
        for (K k : map.keySet()) {
            list.add(map.get(k));
        }
        return list;
    }

    public static <T> List<List<T>> split(List<T> list, int size) {
        List<List<T>> lists = new ArrayList<List<T>>();
        int count = (list.size() / size) + (list.size() % size == 0 ? 0 : 1);
        for (int i = 0; i < count; i++) {
            int fromIndex = i * size, toIndex = (i + 1) * size;
            List<T> subList = list.subList(fromIndex, toIndex > list.size() ? list.size() : toIndex);
            lists.add(subList);
        }
        return lists;
    }

    public static <T> List<T> removeAll(List<T> src, List<T> removes, Comparator<T> comparator) {
        List<T> list = new ArrayList<T>();
        for (T srcItem : src) {
            boolean equal = false;
            for (T removeItem : removes) {
                if (comparator.compare(srcItem, removeItem) == 0) {
                    equal = true;
                    break;
                }
            }
            if (!equal) {
                list.add(srcItem);
            }
        }
        return list;
    }

}
