/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:BaseEnumCustomTypeMapper.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jul 10, 2013 12:05:34 PM
 * 
 */
package com.pixshow.framework.support.mapper;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.support.JdbcUtils;

import com.pixshow.framework.exception.api.SysException;
import com.pixshow.framework.support.BaseEnum;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Jul 10, 2013
 * 
 */

public class BaseEnumCustomTypeProcesor implements CustomTypeProcesor {

    private Map<Class<?>, Class<?>> valueTypes = new HashMap<Class<?>, Class<?>>();

    private Class<?> getValueType(Class<?> propertyType) {
        Class<?> valueType = valueTypes.get(propertyType);
        if (valueType == null) {
            try {
                Method getValueMethod = propertyType.getMethod("getValue", new Class[] {});
                valueType = getValueMethod.getReturnType();
            } catch (Exception e) {
                throw new SysException(e);
            }
            valueTypes.put(propertyType, valueType);
        }
        return valueType;
    }

    @Override
    public Object getObjectValue(ResultSet rs, int index, Class<?> propertyType) throws SQLException {
        Class<?> valueType = getValueType(propertyType);
        Object v = JdbcUtils.getResultSetValue(rs, index, valueType);
        if (v != null) {
            BaseEnum<?>[] enums = (BaseEnum<?>[]) propertyType.getEnumConstants();
            for (BaseEnum<?> n : enums) {
                if (n.getValue().equals(v)) {
                    return n;
                }
            }
        }
        return null;
    }

    @Override
    public Object getDatabaseValue(Object value) {
        return value == null ? null : ((BaseEnum<?>) value).getValue();
    }

    @Override
    public boolean isMatch(Class<?> propertyType) {
        if (BaseEnum.class.isAssignableFrom(propertyType)) {
            return true;
        }
        return false;
    }

}
