/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:JSONObjectCustomTypeMapper.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jul 10, 2013 12:00:41 PM
 * 
 */
package com.pixshow.framework.support.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.support.JdbcUtils;

import com.pixshow.framework.utils.StringUtility;

import net.sf.json.JSONObject;

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

public class JsonlibCustomTypeProcesor implements CustomTypeProcesor {

    @Override
    public Object getObjectValue(ResultSet rs, int index, Class<?> propertyType) throws SQLException {
        Object value = null;
        String text = (String) JdbcUtils.getResultSetValue(rs, index, String.class);
        if (StringUtility.isNotEmpty(text)) {
            value = JSONObject.fromObject(text);
        }
        return value;
    }

    @Override
    public Object getDatabaseValue(Object value) {
        return value == null ? null : ((JSONObject) value).toString();
    }

    @Override
    public boolean isMatch(Class<?> propertyType) {
        if (JSONObject.class.equals(propertyType)) {
            return true;
        }
        return false;
    }
}
