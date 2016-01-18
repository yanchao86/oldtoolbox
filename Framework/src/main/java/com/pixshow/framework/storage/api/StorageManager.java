package com.pixshow.framework.storage.api;

import java.util.HashMap;
import java.util.Map;

import com.pixshow.framework.storage.internal.AliyunStorage;

/**
 * 
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since May 22, 2013
 *
 */
public class StorageManager {

    private static final Map<String, Storage> mapping = new HashMap<String, Storage>();

    private static synchronized void init(String name) {
        if (mapping.get(name) == null) {
            if ("aliyun".equals(name)) {
                mapping.put(name, new AliyunStorage());
            }
        }
    }

    public static Storage getStorage() {
        return getStorage("aliyun");
    }

    public static Storage getStorage(String name) {
        name = name.toLowerCase();
        Storage storage = mapping.get(name);
        if (storage == null) {
            init(name);
        }
        return mapping.get(name);
    }
}
