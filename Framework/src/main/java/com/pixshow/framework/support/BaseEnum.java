/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:BaseEnum.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Mar 13, 2012 1:54:28 PM
 * 
 */
package com.pixshow.framework.support;

import java.io.Serializable;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.5 $ $Date: 2012/09/20 02:44:17 $
 * @since Mar 13, 2012
 * 
 */

public interface BaseEnum<ValueType> extends Serializable {

    public String getLable();

    public ValueType getValue();
}
