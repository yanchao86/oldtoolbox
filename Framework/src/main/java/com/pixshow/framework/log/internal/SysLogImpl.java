/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:SysLogImpl.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jan 16, 2013 11:35:29 AM
 * 
 */
package com.pixshow.framework.log.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.pixshow.framework.log.api.SysLog;

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

public class SysLogImpl implements SysLog {

    private static final String FQCN       = SysLogImpl.class.getName();

    private Class<?>            sourceClazz;
    //    private Object   sourceObject;

    private Logger              sysLog     = null;
    private Logger              generalLog = null;

    public SysLogImpl(Class<?> clazz, Object object) {
        this.sourceClazz = clazz;
        //        this.sourceObject = object;
        this.generalLog = Logger.getLogger(sourceClazz);
        this.sysLog = Logger.getLogger("SysLog");
    }

    public void log(String type, String message) {
        log(type, message, null, null);
    }

    @Override
    public void log(String type, String message, Throwable throwable) {
        log(type, message, null, throwable);
    }

    @Override
    public void log(String type, String message, Map<String, Object> info) {
        log(type, message, info, null);
    }

    @Override
    public void log(String type, String message, Map<String, Object> info, Throwable throwable) {
        if (info == null) {
            info = new HashMap<String, Object>();
        }
        sysLog.log(FQCN, Level.INFO, new SysLogRecord(type, message, info, throwable), throwable);
    }

    @Override
    public void debug(Object message) {
        generalLog.log(FQCN, Level.DEBUG, message, null);
    }

    @Override
    public void debug(Object message, Throwable throwable) {
        generalLog.log(FQCN, Level.DEBUG, message, throwable);
    }

    @Override
    public void error(Object message) {
        generalLog.log(FQCN, Level.ERROR, message, null);

    }

    @Override
    public void error(Object message, Throwable throwable) {
        generalLog.log(FQCN, Level.ERROR, message, throwable);
    }

    @Override
    public void fatal(Object message) {
        generalLog.log(FQCN, Level.FATAL, message, null);
    }

    @Override
    public void fatal(Object message, Throwable throwable) {
        generalLog.log(FQCN, Level.FATAL, message, throwable);
    }

    @Override
    public void info(Object message) {
        generalLog.log(FQCN, Level.INFO, message, null);
    }

    @Override
    public void info(Object message, Throwable throwable) {
        generalLog.log(FQCN, Level.INFO, message, throwable);
    }

    @Override
    public void trace(Object message) {
        generalLog.log(FQCN, Level.DEBUG, message, null);
    }

    @Override
    public void trace(Object message, Throwable throwable) {
        generalLog.log(FQCN, Level.DEBUG, message, throwable);
    }

    @Override
    public void warn(Object message) {
        generalLog.log(FQCN, Level.WARN, message, null);
    }

    @Override
    public void warn(Object message, Throwable throwable) {
        generalLog.log(FQCN, Level.WARN, message, throwable);
    }

    @Override
    public boolean isDebugEnabled() {
        return generalLog.isDebugEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return generalLog.isEnabledFor(Level.ERROR);
    }

    @Override
    public boolean isFatalEnabled() {
        return generalLog.isEnabledFor(Level.FATAL);
    }

    @Override
    public boolean isInfoEnabled() {
        return generalLog.isInfoEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return generalLog.isEnabledFor(Level.DEBUG);
    }

    @Override
    public boolean isWarnEnabled() {
        return generalLog.isEnabledFor(Level.WARN);
    }

}
