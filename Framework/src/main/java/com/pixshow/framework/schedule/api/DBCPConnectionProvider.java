/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:CBCPConnectionProvider.java Project: LvWeatherService
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Apr 8, 2013 2:36:20 PM
 * 
 */
package com.pixshow.framework.schedule.api;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.quartz.utils.ConnectionProvider;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Apr 8, 2013
 * 
 */

public class DBCPConnectionProvider implements ConnectionProvider {

    private BasicDataSource source                        = null;

    private String          url                           = null;
    private String          driverClassName               = null;
    private String          username                      = null;
    private String          password                      = null;

    private int             initialSize                   = 5;
    private int             minIdle                       = 5;
    private int             maxIdle                       = 10;
    private int             maxActive                     = 20;
    private int             maxWait                       = 1000 * 60;           //ms
    private boolean         logAbandoned                  = false;
    private boolean         removeAbandoned               = true;
    private int             removeAbandonedTimeout        = 1200;                //s
    private int             timeBetweenEvictionRunsMillis = 1000 * 60 * 30;      //ms
    private int             minEvictableIdleTimeMillis    = 1000 * 60 * 30;      //ms

    private boolean         testOnBorrow                  = true;
    private String          validationQuery               = "select 1 from dual";

    @Override
    public Connection getConnection() throws SQLException {
        if (source == null) {
            init();
        }
        return source.getConnection();
    }

    private synchronized void init() {
        if (source == null) {
            BasicDataSource dataSource = new BasicDataSource();

            dataSource.setDriverClassName(driverClassName);
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            dataSource.setInitialSize(initialSize);
            dataSource.setMaxIdle(maxIdle);
            dataSource.setMinIdle(minIdle);
            dataSource.setMaxActive(maxActive);
            dataSource.setMaxWait(maxWait);
            dataSource.setLogAbandoned(logAbandoned);
            dataSource.setRemoveAbandoned(removeAbandoned);
            dataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
            dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
            dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
            dataSource.setTestOnBorrow(testOnBorrow);
            dataSource.setValidationQuery(validationQuery);
            source = dataSource;
        }
    }

    @Override
    public void shutdown() throws SQLException {
        source.close();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public boolean isLogAbandoned() {
        return logAbandoned;
    }

    public void setLogAbandoned(boolean logAbandoned) {
        this.logAbandoned = logAbandoned;
    }

    public boolean isRemoveAbandoned() {
        return removeAbandoned;
    }

    public void setRemoveAbandoned(boolean removeAbandoned) {
        this.removeAbandoned = removeAbandoned;
    }

    public int getRemoveAbandonedTimeout() {
        return removeAbandonedTimeout;
    }

    public void setRemoveAbandonedTimeout(int removeAbandonedTimeout) {
        this.removeAbandonedTimeout = removeAbandonedTimeout;
    }

    public int getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public int getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

}
