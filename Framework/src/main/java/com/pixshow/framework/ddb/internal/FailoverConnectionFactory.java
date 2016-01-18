/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:FailoverConnectionFactory.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Apr 17, 2013 6:27:04 PM
 * 
 */
package com.pixshow.framework.ddb.internal;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang.StringUtils;

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

public class FailoverConnectionFactory implements ConnectionFactory {

    private List<Properties> serverList = new ArrayList<Properties>();

    public FailoverConnectionFactory(List<Properties> serverList) {
        this.serverList = serverList;
    }

    @Override
    public Connection createConnection() throws SQLException {
        Connection connection = null;
        for (Properties properties : serverList) {
            String driverClassName = properties.getProperty("driverClassName");
            String validationQuery = properties.getProperty("validationQuery");
            if (StringUtils.isBlank(validationQuery)) {
                validationQuery = "SELECT 1";
            }
            String url = properties.getProperty("url");
            try {
                Driver driver = createDriver(driverClassName, url);
                connection = driver.connect(url, properties);
                if (checkingConnection(connection, validationQuery)) {
                    break;
                } else {
                    DbUtils.closeQuietly(connection);
                }
            } catch (Exception e) {
                e.printStackTrace();
                DbUtils.closeQuietly(connection);
            }
        }
        return connection;
    }

    public boolean checkingConnection(Connection connection, String validationQuery) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(validationQuery);
            if (rs.next()) {
                return true; // connection is valid
            }
        } catch (Exception e) {
            return false;
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(stmt);
        }
        return false;
    }

    protected Driver createDriver(String driverClassName, String url) throws SQLException {
        // Load the JDBC driver class
        Class<?> driverFromCCL = null;
        if (driverClassName != null) {
            try {
                try {
                    Class.forName(driverClassName);
                } catch (ClassNotFoundException cnfe) {
                    driverFromCCL = Thread.currentThread().getContextClassLoader().loadClass(driverClassName);
                }
            } catch (Throwable t) {
                String message = "Cannot load JDBC driver class '" + driverClassName + "'";
                throw new SQLException(message, t);
            }
        }

        // Create a JDBC driver instance
        Driver driver = null;
        try {
            if (driverFromCCL == null) {
                driver = DriverManager.getDriver(url);
            } else {
                // Usage of DriverManager is not possible, as it does not
                // respect the ContextClassLoader
                driver = (Driver) driverFromCCL.newInstance();
                if (!driver.acceptsURL(url)) {
                    throw new SQLException("No suitable driver", "08001");
                }
            }
        } catch (Throwable t) {
            String message = "Cannot create JDBC driver of class '" + (driverClassName != null ? driverClassName : "") + "' for connect URL '" + url + "'";
            throw new SQLException(message, t);
        }
        return driver;
    }

}
