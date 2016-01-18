/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:Cache.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Mar 16, 2012 11:44:24 AM
 * 
 */
package com.pixshow.framework.cache.api;

import java.util.List;

/**
 * 
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.2 $ $Date: 2012/07/12 09:33:55 $
 * @since Mar 21, 2012
 * 
 */
public interface Cache {

    public Object get(String key);

    public List<Object> get(List<String> key);

    public void set(String key, Object value);

    public void set(String key, Object value, int expire);

    public Object delete(String key);

    public boolean keyExists(String key);

}
