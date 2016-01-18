/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:ConfigTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Apr 17, 2013 2:53:13 PM
 * 
 */
package com.pixshow.framework.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.pixshow.framework.utils.NumberUtility;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Apr 17, 2013
 * 
 */

public class ConfigTests {
    public static void main(String[] args) {
        //        Map<String, String> map = Config.getInstance().getGroup("jdbc\\.((\\w+)&(?!readOnly))\\.(\\w+)");
        //        Map<String, String> map = Config.getInstance().getGroup("jdbc\\.(\\w+)");
        //        Map<String, String> map = Config.getInstance().getGroup("jdbc\\.readonly\\.(\\w+)");
        //        Set<String> set = Config.getInstance().getKeys("jdbc\\.readonly\\.(\\d+)\\.\\w+", 1)
        Set<String> keys = Config.getInstance().getKeys("jdbc\\.readonly\\.(\\d+)\\.\\w+", 1);
        List<Integer> nodes = NumberUtility.stringToInt(new ArrayList<String>(keys));
        Collections.sort(nodes);
        System.out.println(nodes);
    }
}
