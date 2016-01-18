/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:Test1Service.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Apr 16, 2013 4:21:30 PM
 * 
 */
package com.pixshow.framework.ddb;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.DateUtility;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Apr 16, 2013
 * 
 */
@Component
public class Test1Service extends BaseDao {

    @Transactional(propagation = Propagation.REQUIRED)
    public void save() {
        getJdbcTemplate().update("insert into t1(name) values(?)", "" + DateUtility.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }
}
