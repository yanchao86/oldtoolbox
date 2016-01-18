/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:RedisTests.java Project: LvFramework
 * 
 * Creator:4399-lvtu-8 
 * Date:Apr 17, 2013 4:43:27 PM
 * 
 */
package com.pixshow.framework.cache.internal.adapter;

import redis.clients.jedis.Jedis;

import com.pixshow.framework.config.Config;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Apr 17, 2013
 * 
 */

public class RedisTests {
    public static void main(String[] args) {
        String host = Config.getInstance().getString("cache.global.host");
        int port = Config.getInstance().getInteger("cache.global.port", 6379);
        String password = Config.getInstance().getString("cache.global.password");
        Jedis jedis = null;
        try {
            jedis = new Jedis(host, port);
            //            jedis.select(database);
            jedis.auth(password);
            jedis.flushAll();
            //            System.out.println(jedis.get("memberRouting@6".getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.disconnect();
            }
        }
    }
}
