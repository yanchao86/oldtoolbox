/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:BeanUtility.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jul 5, 2012 11:08:27 AM
 * 
 */
package com.pixshow.framework.utils;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: gaopeng.300.cn $
 * @version $Revision: 1.4 $ $Date: 2012/08/09 07:53:23 $
 * @since Jul 5, 2012
 * 
 */

public class BeanUtility {

    protected static PropertyDescriptor findSameProperty(PropertyDescriptor propertyDescriptor, PropertyDescriptor[] destDescriptors) {
        for (PropertyDescriptor destPropertyDescriptor : destDescriptors) {
            String name = propertyDescriptor.getName();
            name = name.replaceAll("_", "");
            String destName = destPropertyDescriptor.getName();
            destName = destName.replaceAll("_", "");
            if (name.equalsIgnoreCase(destName)) {
                return destPropertyDescriptor;
            }
        }
        return null;
    }

    public static void copySameProperties(Object dest, Object orig) {
        PropertyDescriptor[] origDescriptors = PropertyUtils.getPropertyDescriptors(orig);
        PropertyDescriptor[] destDescriptors = PropertyUtils.getPropertyDescriptors(dest);
        for (PropertyDescriptor origDescriptor : origDescriptors) {
            PropertyDescriptor destDescriptor = findSameProperty(origDescriptor, destDescriptors);
            if (destDescriptor != null) {
                if (PropertyUtils.isReadable(orig, origDescriptor.getName()) && PropertyUtils.isWriteable(dest, destDescriptor.getName())) {
                    Object value = getSimpleProperty(orig, origDescriptor.getName());
                    setSimpleProperty(dest, destDescriptor.getName(), value);
                }
            }
        }
    }

    public static void copyProperties(Object dest, Object orig) {
        try {
            //BeanUtils.copyProperties(dest, orig);
            PropertyUtils.copyProperties(dest, orig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setProperty(Object bean, String name, Object value) {
        try {
            PropertyUtils.setProperty(bean, name, value);
        } catch (Exception e) {
        }
    }

    public static void setSimpleProperty(Object bean, String name, Object value) {
        try {
            PropertyUtils.setSimpleProperty(bean, name, value);
        } catch (Exception e) {
        }
    }

    public static Object getSimpleProperty(Object bean, String name) {
        try {
            return PropertyUtils.getSimpleProperty(bean, name);
        } catch (Exception e) {
        }
        return null;
    }

    public static Object getProperty(Object bean, String name) {
        try {
            return PropertyUtils.getProperty(bean, name);
        } catch (Exception e) {
        }
        return null;
    }

    public static List<PropertyDescriptor> getPropertyDescriptors(Object bean) {
        PropertyDescriptor[] origDescriptors = PropertyUtils.getPropertyDescriptors(bean);
        return Arrays.asList(origDescriptors);
    }

}
