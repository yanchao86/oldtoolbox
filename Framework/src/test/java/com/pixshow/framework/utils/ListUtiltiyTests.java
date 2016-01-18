/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:ListUtiltiyTests.java Project: LvFramework
 * 
 * Creator:4399-lvtu-8 
 * Date:Apr 24, 2013 12:31:09 PM
 * 
 */
package com.pixshow.framework.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Apr 24, 2013
 * 
 */

public class ListUtiltiyTests {

    public static void test_split() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i <= 15; i++) {
            list.add("" + i);
        }
        System.out.println(ListUtiltiy.split(list, 1));
        System.out.println(ListUtiltiy.split(list, 2));
        System.out.println(ListUtiltiy.split(list, 3));
        System.out.println(ListUtiltiy.split(list, 4));
        System.out.println(ListUtiltiy.split(list, 5));
    }

    public static void main(String[] args) {
        test_split();
    }
}
