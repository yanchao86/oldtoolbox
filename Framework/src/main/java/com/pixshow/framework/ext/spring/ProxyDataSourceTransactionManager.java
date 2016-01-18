/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:ProxyDataSourceTransactionManager.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Mar 12, 2013 2:40:53 PM
 * 
 */
package com.pixshow.framework.ext.spring;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import com.pixshow.framework.cache.api.CacheManager;
import com.pixshow.framework.log.api.SysLog;
import com.pixshow.framework.log.api.SysLogFactory;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Mar 12, 2013
 * 
 */

public class ProxyDataSourceTransactionManager extends DataSourceTransactionManager {

    private static final long serialVersionUID = 1L;
    private SysLog            log              = SysLogFactory.getLog(this);

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        super.doCommit(status);

        if (log.isDebugEnabled()) {
            log.debug("------ doCommit ------");
        }
        if (!status.isReadOnly()) {
            CacheManager.getInstance().transactionCommit();
        }
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        super.doRollback(status);

        if (log.isDebugEnabled()) {
            log.debug("------ doRollback ------");
        }
        if (!status.isReadOnly()) {
            CacheManager.getInstance().transactionRollback();
        }
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        super.doBegin(transaction, definition);
//        JdbcTransactionObjectSupport txObject = (JdbcTransactionObjectSupport) transaction;
//        Connection connection = txObject.getConnectionHolder().getConnection();

        if (log.isDebugEnabled()) {
            log.debug("------ doBegin ------ ReadOnly=" + definition.isReadOnly() + " IsolationLevel=" + definition.getIsolationLevel() + " PropagationBehavior=" + definition.getPropagationBehavior());
        }

        if (!definition.isReadOnly()) {
            CacheManager.getInstance().transactionBegin();
        }
    }
}
