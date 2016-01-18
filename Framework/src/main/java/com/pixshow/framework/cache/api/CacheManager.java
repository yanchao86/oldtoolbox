package com.pixshow.framework.cache.api;

import java.util.HashMap;
import java.util.Map;

import com.pixshow.framework.cache.internal.adapter.ProxyTransactionCache;
import com.pixshow.framework.cache.internal.adapter.RedisCache;
import com.pixshow.framework.cache.internal.adapter.UnableCache;
import com.pixshow.framework.config.Config;
import com.pixshow.framework.exception.api.SysException;

public class CacheManager {

    // private Log log = LogFactory.getLog(CacheManager.class);

    private Map<CacheType, Cache>                              cacheMap            = new HashMap<CacheType, Cache>();
    private ThreadLocal<Map<CacheType, ProxyTransactionCache>> transactionCacheMap = new ThreadLocal<Map<CacheType, ProxyTransactionCache>>();

    private static CacheManager                                instance            = new CacheManager();

    public static CacheManager getInstance() {
        return instance;
    }

    private CacheManager() {
        cacheMap.put(CacheType.Global, isEnable(CacheType.Global) ? new RedisCache() : new UnableCache());
        cacheMap.put(CacheType.GlobalHigh, new UnableCache());
        cacheMap.put(CacheType.App, new UnableCache());
        cacheMap.put(CacheType.AppHigh, new UnableCache());
    }

    public void transactionBegin() {
        transactionCacheMap.set(new HashMap<CacheType, ProxyTransactionCache>());
    }

    public void transactionCommit() {
        Map<CacheType, ProxyTransactionCache> map = transactionCacheMap.get();
        for (CacheType type : map.keySet()) {
            ProxyTransactionCache c = map.get(type);
            if (c != null) {
                try {
                    c.doCommit();
                } catch (Exception e) {
                }
            }
        }
        transactionCacheMap.set(null);
    }

    public void transactionRollback() {
        Map<CacheType, ProxyTransactionCache> map = transactionCacheMap.get();
        for (CacheType type : map.keySet()) {
            ProxyTransactionCache c = map.get(type);
            if (c != null) {
                try {
                    c.doRollback();
                } catch (Exception e) {
                }
            }
        }
        transactionCacheMap.set(null);
    }

    public Cache getCache() {
        return getCache(CacheType.Global); // default
    }

    public Cache getCache(CacheType type) {
        Cache cache = null;
        if (transactionCacheMap.get() != null) {
            cache = transactionCacheMap.get().get(type);
            if (cache == null) {
                cache = cacheMap.get(type);
                if (cache == null) {
                    cache = new ProxyTransactionCache(cache);
                }
            }
        } else {
            cache = cacheMap.get(type);
        }
        if (cache == null) {
            throw new SysException("no match cache type [" + type + "]");
        }
        return cache;
    }

    public static boolean isEnable(CacheType type) {
        Boolean b = Config.getInstance().getBoolean("cache." + type.getKey() + ".enable");
        return b == null ? false : b;
    }
}
