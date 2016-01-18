/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:AnnotationUtility.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:May 18, 2012 1:39:07 PM
 * 
 */
package com.pixshow.framework.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: yangjunshuai.300.cn $
 * @version $Revision: 1.4 $ $Date: 2012/07/17 11:00:25 $
 * @since May 18, 2012
 * 
 */

public class AnnotationUtility extends AnnotationUtils {
    public static <A extends Annotation> List<Method> getAnnotatedMethods(Class<?> clazz, Class<A> annotation) {
        List<Method> methods = new ArrayList<Method>();
        Method[] ms = clazz.getMethods();
        for (Method method : ms) {
            if (findAnnotation(method, annotation) != null)
                methods.add(method);
        }
        return methods;
    }

    public static <A extends Annotation> List<Field> getAnnotatedFields(Class<?> clazz, Class<A> annotation) {
        Class<?> srcClazz = ClassUtils.getUserClass(clazz);
        List<Field> childList = new ArrayList<Field>();
        Field[] fields = srcClazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotation)) {
                childList.add(field);
            }
        }

        if (srcClazz.getSuperclass() != null) {
            List<Field> superList = getAnnotatedFields(srcClazz.getSuperclass(), annotation);
            for (Field superField : superList) {
                boolean exist = false;
                for (Field child : fields) {
                    if (superField.getName().equals(child.getName())) {
                        exist = true;
                        continue;
                    }
                }
                if (!exist) {
                    childList.add(superField);
                }
            }
        }

        return childList;
    }
}

