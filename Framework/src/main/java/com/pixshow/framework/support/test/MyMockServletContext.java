/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:MyMockServletContext.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:May 22, 2012 3:12:54 PM
 * 
 */
package com.pixshow.framework.support.test;

import java.util.Set;

import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockServletContext;

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

public class MyMockServletContext extends MockServletContext {

    public MyMockServletContext(ResourceLoader resourceLoader) {
        super("", resourceLoader);
    }

    public Set<String> getResourcePaths(String path) {
        return null;
    }
}
