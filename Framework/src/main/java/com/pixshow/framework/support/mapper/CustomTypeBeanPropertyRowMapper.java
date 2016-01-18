/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:CustomTypeBeanPropertyRowMapper.java Project: DTaskServer
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jan 9, 2013 6:17:01 PM
 * 
 */
package com.pixshow.framework.support.mapper;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import com.pixshow.framework.exception.api.SysException;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Jan 9, 2013
 * 
 */

public class CustomTypeBeanPropertyRowMapper<T> implements RowMapper<T> {

    private Class<T>                        mappedClass;
    private Map<String, PropertyDescriptor> mappedFields;
    private List<CustomTypeProcesor>        procesors;

    public CustomTypeBeanPropertyRowMapper(Class<T> mappedClass) {
        this.procesors = CustomTypeMapper.getProcesors();
        this.mappedClass = mappedClass;
        this.mappedFields = new HashMap<String, PropertyDescriptor>();
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
        for (PropertyDescriptor pd : pds) {
            if (pd.getWriteMethod() != null) {
                this.mappedFields.put(pd.getName().toLowerCase(), pd);
                String underscoredName = underscoreName(pd.getName());
                if (!pd.getName().toLowerCase().equals(underscoredName)) {
                    this.mappedFields.put(underscoredName, pd);
                }
            }
        }
    }

    private String underscoreName(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            result.append(name.substring(0, 1).toLowerCase());
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                if (s.equals(s.toUpperCase())) {
                    result.append("_");
                    result.append(s.toLowerCase());
                } else {
                    result.append(s);
                }
            }
        }
        return result.toString();
    }

    protected Object getColumnValue(ResultSet rs, int index, String fieldName, PropertyDescriptor pd) throws Exception {
        Class<?> propertyType = pd.getPropertyType();

        Object value = null;

        boolean isCustomType = false;
        for (CustomTypeProcesor procesor : procesors) {
            if (procesor.isMatch(propertyType)) {
                value = procesor.getObjectValue(rs, index, propertyType);
                isCustomType = true;
                break;
            }
        }
        if (!isCustomType) {
            value = JdbcUtils.getResultSetValue(rs, index, propertyType);
        }

        // 原始类型默认值
        if (value == null) {
            if (propertyType.equals(short.class) //
                    || propertyType.equals(int.class) //
                    || propertyType.equals(long.class) //
                    || propertyType.equals(float.class) //
                    || propertyType.equals(double.class)//
                    || propertyType.equals(byte.class)) {
                value = 0;
            } else if (propertyType.equals(boolean.class)) {
                value = false;
            } else if (propertyType.equals(char.class)) {
                value = "";
            }
        }
        return value;
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        T mappedObject = BeanUtils.instantiate(this.mappedClass);
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        for (int index = 1; index <= columnCount; index++) {
            String column = JdbcUtils.lookupColumnName(rsmd, index);
            String fieldName = column.replaceAll(" ", "").toLowerCase();
            PropertyDescriptor pd = this.mappedFields.get(fieldName);
            if (pd != null) {
                try {
                    Object value = getColumnValue(rs, index, fieldName, pd);
                    bw.setPropertyValue(pd.getName(), value);
                } catch (Exception ex) {
                    throw new SysException("Unable to map column " + column + " to property " + pd.getName(), ex);
                }
            }
        }
        return mappedObject;
    }
}
