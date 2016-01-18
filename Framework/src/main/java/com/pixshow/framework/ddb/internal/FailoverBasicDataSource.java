/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:FailoverBasicDataSource.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Apr 17, 2013 6:05:41 PM
 * 
 */
package com.pixshow.framework.ddb.internal;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.ConnectionFactory;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Apr 17, 2013
 * 
 */

public class FailoverBasicDataSource extends BasicDataSource {

    private List<Properties> serverList = new ArrayList<Properties>();

    public FailoverBasicDataSource(List<Properties> serverList) {
        this.serverList = serverList;
    }

    @Override
    protected ConnectionFactory createConnectionFactory() throws SQLException {
        return new FailoverConnectionFactory(serverList);
    }
}
