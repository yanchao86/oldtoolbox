/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:CustomTypeMapper.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jul 10, 2013 11:45:31 AM
 * 
 */
package com.pixshow.framework.support.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

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

public interface CustomTypeProcesor {

    public boolean isMatch(Class<?> propertyType);

    public Object getObjectValue(ResultSet rs, int index, Class<?> propertyType) throws SQLException;

    public Object getDatabaseValue(Object value);
}
