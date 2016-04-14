/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:GlobalHighCache.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Mar 16, 2012 11:44:24 AM
 * 
 */
package com.pixshow.framework.cache.internal.adapter;

import java.util.ArrayList;
import java.util.List;

import com.pixshow.framework.cache.api.Cache;
import com.pixshow.framework.config.Config;
import com.pixshow.framework.log.api.SysLog;
import com.pixshow.framework.log.api.SysLogFactory;
import com.pixshow.framework.utils.SerializationUtility;
import com.pixshow.framework.utils.StringUtility;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 缓存对象
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.2 $ $Date: 2012/07/12 09:33:54 $
 * @since Mar 21, 2012
 * 
 */
public class RedisCache implements Cache {

    private SysLog    log = SysLogFactory.getLog(this);

    private JedisPool pool;

    public RedisCache() {

        String host = Config.getInstance().getString("cache.global.host");
        int port = Config.getInstance().getInteger("cache.global.port", 6379);
        String password = Config.getInstance().getString("cache.global.password");
        int maxActive = Config.getInstance().getInteger("cache.global.maxActive", 10);
        int maxIdle = Config.getInstance().getInteger("cache.global.maxIdle", 10);
        int database = Config.getInstance().getInteger("cache.global.database", 1);

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxActive(maxActive);
        config.setMaxIdle(maxIdle);

        try {
            pool = new JedisPool(config, host, port, 30000, password, database);
        } catch (Exception e) {
            pool = null;
            log.error(e, e);
        }

    }

    @Override
    public Object delete(String key) {
        if (pool == null || StringUtility.isEmpty(key)) {
            return null;
        }
        Jedis jedis = null;
        Object obj = null;
        try {
            jedis = pool.getResource();
            obj = get(key);
            if (obj != null) {
                jedis.del(key.getBytes());
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
            }
        }
        return obj;
    }

    @Override
    public Object get(String key) {
        if (pool == null || StringUtility.isEmpty(key)) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            byte[] bytes = jedis.get(key.getBytes());
            if (bytes == null) {
                return null;
            }
            return SerializationUtility.deserialize(bytes);
        } catch (Exception e) {
            return null;
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
            }
        }
    }

    @Override
    public void set(String key, Object value) {
        if (pool == null || StringUtility.isEmpty(key) || value == null)
            return;

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key.getBytes(), SerializationUtility.serialize(value));
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
            }
        }
    }

    @Override
    public void set(String key, Object value, int expire) {
        if (pool == null || StringUtility.isEmpty(key) || value == null)
            return;

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key.getBytes(), SerializationUtility.serialize(value));
            jedis.expire(key.getBytes(), expire);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
            }
        }
    }

    @Override
    public boolean keyExists(String key) {
        if (pool == null || StringUtility.isEmpty(key)) {
            return false;
        }
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            byte[] bytes = jedis.get(key.getBytes());
            return bytes == null ? false : true;
        } catch (Exception e) {
            return false;
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
            }
        }
    }

    @Override
    public List<Object> get(List<String> keys) {

        List<Object> list = new ArrayList<Object>();

        if (pool == null || keys == null || keys.size() == 0) {// 缓存
            return emptyList(keys.size());
        }

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            byte[][] cacheKeys = new byte[keys.size()][];
            for (int i = 0; i < cacheKeys.length; i++) {
                cacheKeys[i] = keys.get(i).getBytes();
            }
            List<byte[]> datas = jedis.mget(cacheKeys);

            for (byte[] data : datas) {
                Object obj = null;
                if (data != null) {
                    obj = SerializationUtility.deserialize(data);
                }
                list.add(obj);
            }

        } catch (Exception e) {
            return emptyList(keys.size());
        } finally {
            if (jedis != null) {
                pool.returnResource(jedis);
            }
        }
        return list;
    }

    private List<Object> emptyList(int size) {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < size; i++) {
            list.add(null);
        }
        return list;
    }

}
