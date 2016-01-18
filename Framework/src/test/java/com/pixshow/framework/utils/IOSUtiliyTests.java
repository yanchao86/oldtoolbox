/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:IOSUtiliyTests.java Project: LvFramework
 * 
 * Creator:4399-lvtu-8 
 * Date:May 15, 2013 6:55:18 PM
 * 
 */
package com.pixshow.framework.utils;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since May 15, 2013
 * 
 */

public class IOSUtiliyTests {
    public static void test_push() {
        IOSUtiliy.push("PaiXiu", 1000, "8f78eeaf7c4038db292cc06213a9b0fb1241a9ed30721edb50c5705fd8a2bbf3");
        IOSUtiliy.push("PaiXiu", "哥真心帅", 1000, "8f78eeaf7c4038db292cc06213a9b0fb1241a9ed30721edb50c5705fd8a2bbf3");
        IOSUtiliy.push("PaiXiu", "哥真心帅", 1000, "A", "8f78eeaf7c4038db292cc06213a9b0fb1241a9ed30721edb50c5705fd8a2bbf3");
    }

    public static void main(String[] args) {
        test_push();
    }
}
