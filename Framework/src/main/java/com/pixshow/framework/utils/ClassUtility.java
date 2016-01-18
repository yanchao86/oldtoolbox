/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:ClassUtility.java Project: Framework_DEV
 * 
 * Creator:Administrator 
 * Date:2012-6-27 下午03:14:41
 * 
 */
package com.pixshow.framework.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.util.ClassUtils;

/**
 * @author <a href="mailto:yangjunshuai@300.com">yangjunshuai</a>
 * @author $Author: yangjunshuai.300.cn $
 * @version $Revision: 1.4 $ $Date: 2012/07/17 11:00:25 $
 * @since 2012-6-27 下午03:14:41
 * @see Class
 */
public class ClassUtility extends ClassUtils {

    public static Class<?> forName(String className) {
        try {
            return ClassUtils.forName(className, ClassUtility.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getClassName(Class<?> clazz) {
        clazz = getUserClass(clazz);
        return clazz.getName();
    }

    public static Class getGenericClass(Class clazz) {
        return getGenericClass(clazz, 0);
    }

    public static Class getGenericClass(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (genType instanceof ParameterizedType) {
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            if ((params != null) && (params.length >= (index - 1))) {
                return (Class) params[index];
            }
        }
        return null;
    }
}
