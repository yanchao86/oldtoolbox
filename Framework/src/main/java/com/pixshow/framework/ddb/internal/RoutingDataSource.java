/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:RoutingDataSource.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Aug 6, 2012 3:11:45 PM
 * 
 */
package com.pixshow.framework.ddb.internal;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.util.Assert;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.1 $ $Date: 2012/08/09 07:22:02 $
 * @since Aug 6, 2012
 * 
 */

public class RoutingDataSource implements DataSource {

    private DataSourcePool dataSourcePool;

    @Override
    public Connection getConnection() throws SQLException {
        return new RoutingConnection(dataSourcePool);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return new RoutingConnection(dataSourcePool, username, password);
    }

    /**
     * Returns 0, indicating the default system timeout is to be used.
     */
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    /**
     * Setting a login timeout is not supported.
     */
    public void setLoginTimeout(int timeout) throws SQLException {
        throw new UnsupportedOperationException("setLoginTimeout");
    }

    /**
     * LogWriter methods are not supported.
     */
    public PrintWriter getLogWriter() {
        throw new UnsupportedOperationException("getLogWriter");
    }

    /**
     * LogWriter methods are not supported.
     */
    public void setLogWriter(PrintWriter pw) throws SQLException {
        throw new UnsupportedOperationException("setLogWriter");
    }

    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) throws SQLException {
        Assert.notNull(iface, "Interface argument must not be null");
        if (!DataSource.class.equals(iface)) {
            throw new SQLException("DataSource of type [" + getClass().getName() + "] can only be unwrapped as [javax.sql.DataSource], not as [" + iface.getName());
        }
        return (T) this;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return DataSource.class.equals(iface);
    }

    /**
     * @return the dataSourcePool
     */
    public DataSourcePool getDataSourcePool() {
        return dataSourcePool;
    }

    /**
     * @param dataSourcePool
     *            the dataSourcePool to set
     */
    public void setDataSourcePool(DataSourcePool dataSourcePool) {
        this.dataSourcePool = dataSourcePool;
    }

}