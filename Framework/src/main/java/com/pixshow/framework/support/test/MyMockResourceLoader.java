/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:MockResourceLoader.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:May 22, 2012 3:19:32 PM
 * 
 */
package com.pixshow.framework.support.test;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.1 $ $Date: 2012/05/22 08:03:56 $
 * @since May 22, 2012
 * 
 */

public class MyMockResourceLoader implements ResourceLoader {

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.core.io.ResourceLoader#getClassLoader()
     */
    @Override
    public ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.core.io.ResourceLoader#getResource(java.lang.String)
     */
    @Override
    public Resource getResource(String location) {
        return new ClassPathResource(location);
    }

}
