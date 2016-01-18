/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:DDBCentext.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:May 30, 2013 2:44:56 PM
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
 * @since May 30, 2013
 * 
 */

public class DDBCentext {
    private Sharding sharding;
    private String   rule;
    private Object[] args;

    public Sharding getSharding() {
        return sharding;
    }

    public void setSharding(Sharding sharding) {
        this.sharding = sharding;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

}
