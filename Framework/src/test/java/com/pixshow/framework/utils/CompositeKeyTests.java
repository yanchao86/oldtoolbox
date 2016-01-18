/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:CompositeKeyTests.java Project: LvFramework
 * 
 * Creator:4399-lvtu-8 
 * Date:Apr 17, 2013 11:19:59 AM
 * 
 */
package com.pixshow.framework.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Apr 17, 2013
 * 
 */

public class CompositeKeyTests {

    private static Map<CompositeKey, String> testDate() {
        Map<CompositeKey, String> map = new HashMap<CompositeKey, String>();
        CompositeKey key1 = new CompositeKey("A", "B");
        map.put(key1, "123");
        return map;
    }

    public static void main(String[] args) {

        Map<CompositeKey, String> map = testDate();

        CompositeKey key1 = new CompositeKey("A", "B");

        System.out.println(map.get(key1));

    }
}
