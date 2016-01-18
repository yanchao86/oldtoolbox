/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:MongodbAppender.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jan 16, 2013 11:43:17 AM
 * 
 */
package com.pixshow.framework.log.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.Vector;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.ServerAddress;
import com.pixshow.framework.utils.DateUtility;
import com.pixshow.framework.utils.GenericIgnoreExceptionTimerTask;
import com.pixshow.framework.utils.StringUtility;

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

public class MongodbAppender extends AppenderSkeleton implements Appender, Runnable {

    protected String               address         = null;
    protected String               username        = null;
    protected String               password        = null;
    protected String               dbName          = null;
    protected Mongo                mongo           = null;
    protected DB                   db              = null;

    protected int                  bufferSize      = 100;
    protected int                  tempBufferSize  = 100;

    protected long                 flushBufferTime = 0;

    protected int                  checkPeriod     = 500;
    protected int                  flushBufferIdle = 30000;

    protected Vector<LoggingEvent> buffer          = null;
    protected Vector<LoggingEvent> tempBuffer      = null;

    protected Timer                timer           = null;

    public MongodbAppender() {
    }

    @Override
    public void activateOptions() {
        buffer = new Vector<LoggingEvent>(bufferSize);
        tempBuffer = new Vector<LoggingEvent>(tempBufferSize);
        timer = new Timer("MongodbAppender-flushBuffer");
        timer.schedule(new GenericIgnoreExceptionTimerTask(this), 0, 500);
        super.activateOptions();
    }

    @Override
    protected void append(LoggingEvent event) {
        buffer.add(event);
    }

    protected void closeDb() {
        if (mongo != null) {
            try {
                mongo.close();
            } catch (Exception e1) {
            }
        }
        mongo = null;
        db = null;
    }

    protected DB getDB() {
        if (db == null) {
            try {
                String[] hosts = StringUtility.split(address, ",");
                List<ServerAddress> addresses = new ArrayList<ServerAddress>();
                for (String host : hosts) {
                    addresses.add(new ServerAddress(host));
                }
                mongo = new Mongo(addresses);

                db = mongo.getDB(dbName);
                boolean auth = db.authenticate(username, password.toCharArray());
                if (!auth) {
                    System.out.println("Mongodb : invalid password.");
                    closeDb();
                }
            } catch (Exception e) {
                closeDb();
                e.fillInStackTrace();
            }
        }
        return db;
    }

    public synchronized void flushBuffer() {
        List<LoggingEvent> removes = new ArrayList<LoggingEvent>();
        DB db = getDB();
        int length = buffer.size();
        if (db == null) {
            for (int i = 0; i < length; i++) {
                LoggingEvent logEvent = buffer.get(i);
                tempBuffer.add(logEvent);
                removes.add(logEvent);
                if (tempBuffer.size() > tempBufferSize) {
                    process(tempBuffer.remove(0), null);
                }
            }
        } else {
            for (int i = 0; i < length; i++) {
                LoggingEvent logEvent = buffer.get(i);
                process(logEvent, db);
                removes.add(logEvent);
            }
            while (!tempBuffer.isEmpty()) {
                process(tempBuffer.remove(0), db);
            }
        }
        // remove from the buffer any events that were reported
        if (removes.size() > 0) {
            buffer.removeAll(removes);
        }
        // clear the buffer of reported events
        flushBufferTime = System.currentTimeMillis();
    }

    private void process(LoggingEvent logEvent, DB db) {
        try {
            Object message = logEvent.getMessage();
            if (message instanceof SysLogRecord) {
                SysLogRecord record = (SysLogRecord) message;
                if (db != null) {
                    DBCollection collection = db.getCollection("log_" + record.getType());
                    BasicDBObject object = new BasicDBObject();
                    object.put("message", record.getMessage() == null ? null : record.getMessage().toString());
                    object.put("timestamp", logEvent.timeStamp);
                    object.put("throwable", ExceptionUtils.getFullStackTrace(record.getThrowable()));
                    object.putAll(record.getInfo());
                    collection.insert(object);
                } else {
                    System.out.println("Syslog " + record.getType() + " (" + DateUtility.format(new Date(logEvent.timeStamp), "yyyy-MM-dd HH:mm:ss") + ") -> " + record.getMessage() + " INFO:" + record.getInfo());
                }
            } else {
                if (db != null) {
                    DBCollection collection = db.getCollection("log_error");
                    BasicDBObject object = new BasicDBObject();
                    object.put("message", message == null ? null : message.toString());
                    object.put("timestamp", logEvent.timeStamp);

                    StringBuilder throwable = new StringBuilder();
                    if (logEvent.getThrowableStrRep() != null) {
                        for (String str : logEvent.getThrowableStrRep()) {
                            throwable.append(str);
                        }
                    }
                    object.put("throwable", throwable.toString());

                    collection.insert(object);
                } else {
                    System.out.println(logEvent.getLevel() + " (" + DateUtility.format(new Date(logEvent.timeStamp), "yyyy-MM-dd HH:mm:ss") + ") -> " + message.toString());
                    String[] throwable = logEvent.getThrowableStrRep();
                    for (String string : throwable) {
                        System.out.println("\t" + string);
                    }
                }
            }
        } catch (Exception e) {
            errorHandler.error("Failed to excute", e, ErrorCode.FLUSH_FAILURE);
        }
    }

    @Override
    public void close() {
        timer.cancel();
        flushBuffer();
        if (mongo != null) {
            mongo.close();
        }
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public void run() {
        if (buffer.size() > bufferSize || (System.currentTimeMillis() - flushBufferTime) > 30000) {
            flushBuffer();
        }
    }
}
