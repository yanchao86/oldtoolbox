/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:RoutingConnection.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Aug 6, 2012 3:51:41 PM
 * 
 */
package com.pixshow.framework.ddb.internal;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import com.pixshow.framework.ddb.api.DDBCentext;
import com.pixshow.framework.ddb.api.DDBCentextHolder;
import com.pixshow.framework.exception.api.SysException;
import com.pixshow.framework.log.api.SysLog;
import com.pixshow.framework.log.api.SysLogFactory;
import com.pixshow.framework.utils.StringUtility;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: gaopeng.300.cn $
 * @version $Revision: 1.7 $ $Date: 2012/10/23 07:23:12 $
 * @since Aug 6, 2012
 * 
 */

public class RoutingConnection implements Connection {
    private static final String     DEFAULT_KEY       = "defalut";

    private SysLog                  log               = SysLogFactory.getLog(this);

    private Boolean                 autoCommit        = null;
    private Boolean                 readOnly          = null;

    private String                  username          = null;
    private String                  password          = null;
    private DataSourcePool          dataSourcePool    = null;

    private Map<String, Connection> mappingConnection = new HashMap<String, Connection>();

    public RoutingConnection(DataSourcePool dataSourcePool) {
        this.dataSourcePool = dataSourcePool;
    }

    public RoutingConnection(DataSourcePool dataSourcePool, String username, String password) {
        this.dataSourcePool = dataSourcePool;
        this.username = username;
        this.password = password;
    }

    private Connection getTargetConnection() {
        DDBCentext centext = DDBCentextHolder.get();

        String key = DEFAULT_KEY;
        if (centext != null && StringUtility.isNotEmpty(centext.getSharding().getDbName())) {
            key = centext.getSharding().getDbName();
        }

        if (log.isDebugEnabled()) {
            log.debug(" getTargetConnection = " + key);
        }

        Connection connection = mappingConnection.get(key);
        if (connection == null) {
            try {
                connection = dataSourcePool.getConnection(key, username, password, readOnly == null ? false : readOnly);
                if (autoCommit != null)
                    connection.setAutoCommit(autoCommit);
                if (readOnly != null) {
                    connection.setReadOnly(readOnly);
                }
                mappingConnection.put(key, connection);
            } catch (SQLException e) {
                throw new SysException("", e);
            }
        }
        return connection;
    }

    @Override
    public void close() throws SQLException {
        for (Entry<String, Connection> entry : mappingConnection.entrySet()) {
            Connection conn = entry.getValue();
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
        mappingConnection.clear();
        autoCommit = null;
        readOnly = null;
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        this.autoCommit = autoCommit;
        Collection<Connection> all = mappingConnection.values();
        for (Connection conn : all) {
            conn.setAutoCommit(autoCommit);
        }
    }

    @Override
    public void commit() throws SQLException {
        Collection<Connection> all = mappingConnection.values();
        for (Connection conn : all) {
            if (!conn.getAutoCommit())
                conn.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        Collection<Connection> all = mappingConnection.values();
        for (Connection conn : all) {
            conn.rollback();
        }
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        Collection<Connection> all = mappingConnection.values();
        for (Connection conn : all) {
            conn.rollback(savepoint);
        }
    }

    @Override
    public void clearWarnings() throws SQLException {
        getTargetConnection().clearWarnings();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return getTargetConnection().createArrayOf(typeName, elements);
    }

    @Override
    public Blob createBlob() throws SQLException {
        return getTargetConnection().createBlob();
    }

    @Override
    public Clob createClob() throws SQLException {
        return getTargetConnection().createClob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return getTargetConnection().createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return getTargetConnection().createSQLXML();
    }

    @Override
    public Statement createStatement() throws SQLException {
        return new DelegatingStatement(getTargetConnection().createStatement(), DDBCentextHolder.get());
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return new DelegatingStatement(getTargetConnection().createStatement(resultSetType, resultSetConcurrency), DDBCentextHolder.get());
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return new DelegatingStatement(getTargetConnection().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability), DDBCentextHolder.get());
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return getTargetConnection().createStruct(typeName, attributes);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return autoCommit == null ? true : autoCommit;
    }

    @Override
    public String getCatalog() throws SQLException {
        return getTargetConnection().getCatalog();
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return getTargetConnection().getClientInfo();
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return getTargetConnection().getClientInfo(name);
    }

    @Override
    public int getHoldability() throws SQLException {
        return getTargetConnection().getHoldability();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return getTargetConnection().getMetaData();
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return getTargetConnection().getTransactionIsolation();
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return getTargetConnection().getTypeMap();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return getTargetConnection().getWarnings();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return getTargetConnection().isClosed();
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return getTargetConnection().isReadOnly();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return getTargetConnection().isValid(timeout);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return getTargetConnection().nativeSQL(SqlConverter.sql(sql, DDBCentextHolder.get()));
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return getTargetConnection().prepareCall(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getTargetConnection().prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getTargetConnection().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        String newSql = SqlConverter.sql(sql, DDBCentextHolder.get());
        return new DelegatingPreparedStatement(getTargetConnection().prepareStatement(newSql), newSql, DDBCentextHolder.get());
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        String newSql = SqlConverter.sql(sql, DDBCentextHolder.get());
        return new DelegatingPreparedStatement(getTargetConnection().prepareStatement(newSql, autoGeneratedKeys), newSql, DDBCentextHolder.get());
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        String newSql = SqlConverter.sql(sql, DDBCentextHolder.get());
        return new DelegatingPreparedStatement(getTargetConnection().prepareStatement(newSql, columnIndexes), newSql, DDBCentextHolder.get());
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        String newSql = SqlConverter.sql(sql, DDBCentextHolder.get());
        return new DelegatingPreparedStatement(getTargetConnection().prepareStatement(newSql, columnNames), newSql, DDBCentextHolder.get());
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        String newSql = SqlConverter.sql(sql, DDBCentextHolder.get());
        return new DelegatingPreparedStatement(getTargetConnection().prepareStatement(newSql, resultSetType, resultSetConcurrency), newSql, DDBCentextHolder.get());
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        String newSql = SqlConverter.sql(sql, DDBCentextHolder.get());
        return new DelegatingPreparedStatement(getTargetConnection().prepareStatement(newSql, resultSetType, resultSetConcurrency, resultSetHoldability), newSql, DDBCentextHolder.get());
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        getTargetConnection().releaseSavepoint(savepoint);
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        getTargetConnection().setCatalog(catalog);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        getTargetConnection().setClientInfo(properties);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        getTargetConnection().setClientInfo(name, value);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        getTargetConnection().setHoldability(holdability);
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        this.readOnly = readOnly;
        getTargetConnection().setReadOnly(readOnly);
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return getTargetConnection().setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return getTargetConnection().setSavepoint(name);
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        getTargetConnection().setTransactionIsolation(level);
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        getTargetConnection().setTypeMap(map);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return getTargetConnection().isWrapperFor(iface);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return getTargetConnection().unwrap(iface);
    }

}