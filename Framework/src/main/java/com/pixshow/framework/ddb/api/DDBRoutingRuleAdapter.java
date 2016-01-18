/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:RuleAdapter.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jan 4, 2013 5:36:10 PM
 * 
 */
package com.pixshow.framework.ddb.api;

import com.pixshow.framework.ddb.api.annotation.DistributedDataSource;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Jan 4, 2013
 * 
 */

public interface DDBRoutingRuleAdapter {

    public Sharding getSharding(DistributedDataSource ann, Object[] args);

    public String getTableName(String tableName, Sharding sharding);

}
