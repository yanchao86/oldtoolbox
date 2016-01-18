/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:DistributedDataSourceHolder.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Aug 6, 2012 3:25:48 PM
 * 
 */
package com.pixshow.framework.ddb.api;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: yangjunshuai.300.cn $
 * @version $Revision: 1.3 $ $Date: 2012/09/24 04:47:19 $
 * @since Aug 6, 2012
 * 
 */

public class DDBCentextHolder {

    private static ThreadLocal<DDBCentext> threadLocal = new ThreadLocal<DDBCentext>();

    public static void initThreadLocal() {
        threadLocal.set(null);
    }

    public static DDBCentext get() {
        return threadLocal.get();
    }

    public static void set(DDBCentext centext) {
        threadLocal.set(centext);
    }
}
