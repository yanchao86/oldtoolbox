/*
 * Copyright (c) 2010-2012 www.pixshow.net All Rights Reserved
 *
 * File:SerializeVSjson.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Nov 28, 2012 11:35:34 PM
 * 
 */
package com.pixshow.framework.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.pixshow.framework.utils.SerializationUtility;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Nov 28, 2012
 * 
 */

public class SerializeVSjson {

    public static void testSerialize() {
        List<byte[]> datas = new ArrayList<byte[]>();
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            datas.add(SerializationUtility.serialize(new Date(random.nextLong())));
        }

        long time1 = System.currentTimeMillis();
        for (byte[] data : datas) {
            SerializationUtility.deserialize(data);
        }
        long time2 = System.currentTimeMillis();
        System.out.println("testSerialize -> " + (time2 - time1));
    }

    public static void main(String[] args) {
        testSerialize();
    }
}
