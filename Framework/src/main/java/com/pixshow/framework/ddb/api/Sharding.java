/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:Sharding.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jan 22, 2013 4:53:47 PM
 * 
 */
package com.pixshow.framework.ddb.api;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Jan 22, 2013
 * 
 */

public class Sharding {

    public String dbName;
    public String schema;
    public Object property;

    public Sharding() {
    }

    public Sharding(String dbName, String schema, Object property) {
        this.dbName = dbName;
        this.schema = schema;
        this.property = property;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public Object getProperty() {
        return property;
    }

    public void setProperty(Object property) {
        this.property = property;
    }

}
