/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:SysLog.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jan 16, 2013 10:59:12 AM
 * 
 */
package com.pixshow.framework.log.internal;

import java.util.Map;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Jan 16, 2013
 * 
 */

public class SysLogRecord {
    private String              type;
    private String              message;
    private Map<String, Object> info;
    private Throwable           throwable;

    public SysLogRecord(String type, String message, Map<String, Object> info, Throwable throwable) {
        this.type = type;
        this.message = message;
        this.info = info;
        this.throwable = throwable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getInfo() {
        return info;
    }

    public void setInfo(Map<String, Object> info) {
        this.info = info;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("type:").append(type).append(" ");
        buf.append("message:").append(message).append(" ");
        buf.append("info:").append(info).append(" ");
        return buf.toString();
    }

}
