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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.framework.support.BaseDao;

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
public class Test2Service extends BaseDao {

    @Autowired
    private Test1Service test1Service;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void query() {
        System.out.println(getJdbcTemplate().queryForList("select * from t1"));
//        test1Service.save();
    }

}
