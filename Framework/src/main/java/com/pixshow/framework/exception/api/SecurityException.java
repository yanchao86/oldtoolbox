/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:NoLoginException.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:May 17, 2012 4:18:58 PM
 * 
 */
package com.pixshow.framework.exception.api;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.2 $ $Date: 2012/07/12 09:33:58 $
 * @since May 17, 2012
 * 
 */

public class SecurityException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 6772006553291150019L;

    public SecurityException() {
        super("未登录");
    }
}
