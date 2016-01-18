/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:CustomTypeMapper.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jul 10, 2013 1:43:19 PM
 * 
 */
package com.pixshow.framework.support.mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Jul 10, 2013
 * 
 */

public class CustomTypeMapper {
    public static List<CustomTypeProcesor> getProcesors() {
        List<CustomTypeProcesor> procesors = new ArrayList<CustomTypeProcesor>();
        procesors.add(new JsonlibCustomTypeProcesor());
        procesors.add(new BaseEnumCustomTypeProcesor());
        return procesors;
    }
}
