/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:TestService.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jun 6, 2013 3:37:57 PM
 * 
 */
package com.pixshow.framework.script;

import org.springframework.stereotype.Service;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Jun 6, 2013
 * 
 */

@Service
public class TestService {
    public void save(Object obj) {
        System.out.println("save -> " + obj.getClass());
        System.out.println(obj.toString());
    }
}
