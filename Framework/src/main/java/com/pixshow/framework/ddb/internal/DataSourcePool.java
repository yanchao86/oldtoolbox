/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:PoolDataSource.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Aug 6, 2012 4:13:43 PM
 * 
 */
package com.pixshow.framework.ddb.internal;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pixshow.framework.config.Config;
import com.pixshow.framework.exception.api.SysException;
import com.pixshow.framework.utils.NumberUtility;
import com.pixshow.framework.utils.StringUtility;

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
public class DataSourcePool {

    private Log                     log                           = LogFactory.getLog(DataSourcePool.class);

    private DataSource              defaultWriteDataSource        = null;
    private DataSource              defaultReadDataSource         = null;

    private Map<String, DataSource> dataSourceMapping             = new HashMap<String, DataSource>();

    // ============================== default ==============================
    private int                     initialSize                   = 1;
    private int                     maxIdle                       = 10;
    private int                     minIdle                       = 5;
    private int                     maxActive                     = 25;
    private int                     maxWait                       = 1000 * 60;                              //ms
    private boolean                 logAbandoned                  = false;
    private boolean                 removeAbandoned               = true;
    private int                     removeAbandonedTimeout        = 1200;                                   //s
    private int                     timeBetweenEvictionRunsMillis = 1000 * 60 * 30;                         //ms
    private int                     minEvictableIdleTimeMillis    = 1000 * 60 * 30;                         //ms
    private boolean                 testOnBorrow                  = true;
    private String                  validationQuery               = "select 1";
    private int                     validationQueryTimeout        = 15;                                     //s

    // ============================== default ==============================

    public DataSourcePool() {
        initDefaultDataSource();
        initDataSourceMapping();
    }

    private void initDefaultDataSource() {
        defaultWriteDataSource = createWriteDataSource(null);
        defaultReadDataSource = createReadonlyDataSource(null);
    }

    private void initDataSourceMapping() {
        List<String> keys = Config.getInstance().getKeys();
        Pattern pattern = Pattern.compile("jdbc\\.(\\w+)\\.write\\.\\w+"); // jdbc.xxx.write.driverClassName

        Set<String> datasourceNames = new HashSet<String>();
        for (String key : keys) {
            Matcher matcher = pattern.matcher(key);
            if (matcher.matches()) {
                datasourceNames.add(matcher.group(1));
            }
        }

        for (String datasourceName : datasourceNames) {
            DataSource write = createWriteDataSource(datasourceName);
            DataSource read = createReadonlyDataSource(datasourceName);
            dataSourceMapping.put(datasourceName, write);
            if (read != null) {
                dataSourceMapping.put(datasourceName + ".readOnly", read);
            }
        }

    }

    private BasicDataSource createWriteDataSource(String datasourceName) {
        Config config = Config.getInstance();
        String configPrefix = "jdbc." + (StringUtility.isEmpty(datasourceName) ? "" : (datasourceName + ".")) + "write.";

        List<Properties> servers = new ArrayList<Properties>();
        Properties properties = new Properties();
        properties.put("driverClassName", config.getString(configPrefix + "driverClassName"));
        properties.put("url", config.getString(configPrefix + "url"));
        properties.put("user", config.getString(configPrefix + "username"));
        properties.put("password", config.getString(configPrefix + "password"));
        servers.add(properties);

        FailoverBasicDataSource dataSource = new FailoverBasicDataSource(servers);
        //dataSource.setDriverClassName(config.getString(configPrefix + "driverClassName"));
        //dataSource.setUrl(config.getString(configPrefix + "url"));
        //dataSource.setUsername(config.getString(configPrefix + "username"));
        dataSource.setPassword(config.getString(configPrefix + "password"));
        dataSource.setInitialSize(config.getInteger(configPrefix + "initialSize", initialSize));
        dataSource.setMaxIdle(config.getInteger(configPrefix + "maxIdle", maxIdle));
        dataSource.setMinIdle(config.getInteger(configPrefix + "minIdle", minIdle));
        dataSource.setMaxActive(config.getInteger(configPrefix + "maxActive", maxActive));
        dataSource.setMaxWait(config.getInteger(configPrefix + "maxWait", maxWait));
        dataSource.setLogAbandoned(config.getBoolean(configPrefix + "logAbandoned", logAbandoned));
        dataSource.setRemoveAbandoned(config.getBoolean(configPrefix + "removeAbandoned", removeAbandoned));
        dataSource.setRemoveAbandonedTimeout(config.getInteger(configPrefix + "removeAbandonedTimeout", removeAbandonedTimeout));
        dataSource.setTimeBetweenEvictionRunsMillis(config.getInteger(configPrefix + "timeBetweenEvictionRunsMillis", timeBetweenEvictionRunsMillis));
        dataSource.setMinEvictableIdleTimeMillis(config.getInteger(configPrefix + "minEvictableIdleTimeMillis", minEvictableIdleTimeMillis));

        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setValidationQueryTimeout(validationQueryTimeout);

        if (log.isInfoEnabled()) {
            String info = "Create Write DataSource Name = " + datasourceName + "\r\n";
            info += ("    url = " + properties.getProperty("url") + " user = " + properties.getProperty("user") + "\r\n");
            log.info(info);
        }

        return dataSource;

    }

    private BasicDataSource createReadonlyDataSource(String datasourceName) {
        Config config = Config.getInstance();

        String configPrefix = "jdbc." + (StringUtility.isEmpty(datasourceName) ? "" : (datasourceName + ".")) + "readonly.";

        Set<String> serverKeys = config.getKeys("jdbc\\." + (StringUtility.isEmpty(datasourceName) ? "" : (datasourceName + "\\.")) + "readonly\\.(\\d+)\\.\\w+", 1);
        if (serverKeys.size() == 0) {
            return null;
        }

        List<Integer> serverNames = NumberUtility.stringToInt(new ArrayList<String>(serverKeys));
        Collections.sort(serverNames);
        List<Properties> servers = new ArrayList<Properties>();
        for (Integer serverName : serverNames) {
            Properties properties = new Properties();
            properties.put("driverClassName", config.getString(configPrefix + serverName + "." + "driverClassName"));
            properties.put("url", config.getString(configPrefix + serverName + "." + "url"));
            properties.put("user", config.getString(configPrefix + serverName + "." + "username"));
            properties.put("password", config.getString(configPrefix + serverName + "." + "password"));
            servers.add(properties);
        }

        FailoverBasicDataSource dataSource = new FailoverBasicDataSource(servers);
        dataSource.setInitialSize(config.getInteger(configPrefix + "initialSize", initialSize));
        dataSource.setMaxIdle(config.getInteger(configPrefix + "maxIdle", maxIdle));
        dataSource.setMinIdle(config.getInteger(configPrefix + "minIdle", minIdle));
        dataSource.setMaxActive(config.getInteger(configPrefix + "maxActive", maxActive));
        dataSource.setMaxWait(config.getInteger(configPrefix + "maxWait", maxWait));
        dataSource.setLogAbandoned(config.getBoolean(configPrefix + "logAbandoned", logAbandoned));
        dataSource.setRemoveAbandoned(config.getBoolean(configPrefix + "removeAbandoned", removeAbandoned));
        dataSource.setRemoveAbandonedTimeout(config.getInteger(configPrefix + "removeAbandonedTimeout", removeAbandonedTimeout));
        dataSource.setTimeBetweenEvictionRunsMillis(config.getInteger(configPrefix + "timeBetweenEvictionRunsMillis", timeBetweenEvictionRunsMillis));
        dataSource.setMinEvictableIdleTimeMillis(config.getInteger(configPrefix + "minEvictableIdleTimeMillis", minEvictableIdleTimeMillis));

        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setValidationQueryTimeout(validationQueryTimeout);

        if (log.isInfoEnabled()) {
            String info = "Create Readonly DataSource Name = " + datasourceName + "\r\n";
            for (Properties properties : servers) {
                info += ("    url = " + properties.getProperty("url") + " user = " + properties.getProperty("user") + "\r\n");
            }
            log.info(info);
        }
        return dataSource;
    }

    public Connection getConnection(String name, boolean readOnly) {
        return getConnection(name, null, null, readOnly);
    }

    public Connection getConnection(String name, String user, String password, boolean readOnly) {
        Connection connection = null;
        DataSource dataSource = null;

        if (readOnly) {
            dataSource = dataSourceMapping.get(name + ".readOnly");
        }

        if (dataSource == null) {
            dataSource = dataSourceMapping.get(name);
        }

        if (dataSource == null) {
            if (readOnly) {
                dataSource = defaultReadDataSource;
            }
            if (dataSource == null) {
                dataSource = defaultWriteDataSource;
            }
        }

        try {
            if (StringUtility.isNotEmpty(user)) {
                connection = dataSource.getConnection(user, password);
            } else {
                connection = dataSource.getConnection();
            }
        } catch (SQLException e) {
            throw new SysException("", e);
        }
        return connection;
    }

    /**
     * @return the initialSize
     */
    public int getInitialSize() {
        return initialSize;
    }

    /**
     * @param initialSize
     *            the initialSize to set
     */
    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    /**
     * @return the maxIdle
     */
    public int getMaxIdle() {
        return maxIdle;
    }

    /**
     * @param maxIdle
     *            the maxIdle to set
     */
    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    /**
     * @return the minIdle
     */
    public int getMinIdle() {
        return minIdle;
    }

    /**
     * @param minIdle
     *            the minIdle to set
     */
    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    /**
     * @return the maxActive
     */
    public int getMaxActive() {
        return maxActive;
    }

    /**
     * @param maxActive
     *            the maxActive to set
     */
    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    /**
     * @return the maxWait
     */
    public int getMaxWait() {
        return maxWait;
    }

    /**
     * @param maxWait
     *            the maxWait to set
     */
    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    /**
     * @return the logAbandoned
     */
    public boolean isLogAbandoned() {
        return logAbandoned;
    }

    /**
     * @param logAbandoned
     *            the logAbandoned to set
     */
    public void setLogAbandoned(boolean logAbandoned) {
        this.logAbandoned = logAbandoned;
    }

    /**
     * @return the removeAbandoned
     */
    public boolean isRemoveAbandoned() {
        return removeAbandoned;
    }

    /**
     * @param removeAbandoned
     *            the removeAbandoned to set
     */
    public void setRemoveAbandoned(boolean removeAbandoned) {
        this.removeAbandoned = removeAbandoned;
    }

    /**
     * @return the removeAbandonedTimeout
     */
    public int getRemoveAbandonedTimeout() {
        return removeAbandonedTimeout;
    }

    /**
     * @param removeAbandonedTimeout
     *            the removeAbandonedTimeout to set
     */
    public void setRemoveAbandonedTimeout(int removeAbandonedTimeout) {
        this.removeAbandonedTimeout = removeAbandonedTimeout;
    }

    /**
     * @return the timeBetweenEvictionRunsMillis
     */
    public int getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    /**
     * @param timeBetweenEvictionRunsMillis
     *            the timeBetweenEvictionRunsMillis to set
     */
    public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    /**
     * @return the minEvictableIdleTimeMillis
     */
    public int getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    /**
     * @param minEvictableIdleTimeMillis
     *            the minEvictableIdleTimeMillis to set
     */
    public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    /**
     * @return the testOnBorrow
     */
    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    /**
     * @param testOnBorrow
     *            the testOnBorrow to set
     */
    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

}
