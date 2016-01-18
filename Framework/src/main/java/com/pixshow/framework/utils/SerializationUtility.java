/*
 * Copyright (c) 2010-2012 www.pixshow.net All Rights Reserved
 *
 * File:SerializationUtility.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Nov 8, 2012 4:53:33 PM
 * 
 */
package com.pixshow.framework.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.pixshow.framework.exception.api.SysException;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$
 * @since Nov 8, 2012
 * 
 */

public class SerializationUtility {
    public static Object deserialize(byte[] bytes) {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteStream);
            return objectInputStream.readObject();
        } catch (Exception ex) {
            throw new SysException("", ex);
        }
    }

    public static byte[] serialize(Object object) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(128);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            return byteStream.toByteArray();
        } catch (Throwable ex) {
            throw new SysException("", ex);
        }
    }
}
