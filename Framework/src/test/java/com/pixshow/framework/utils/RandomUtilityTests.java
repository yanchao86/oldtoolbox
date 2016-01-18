/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:RandomUtilityTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Nov 6, 2013 6:35:33 PM
 * 
 */
package com.pixshow.framework.utils;

import java.util.Arrays;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Nov 6, 2013
 * 
 */

public class RandomUtilityTests {

    public static void test_nextWeightIndex() {
        int[] weights = {1, 99};
        int[] counts = { 0, 0};
        for (int i = 0; i < 100; i++) {
            int weightIndex = RandomUtility.nextWeightIndex(weights);
            //System.out.println(weightIndex);
            counts[weightIndex]++;
        }
        System.out.println(Arrays.toString(counts));
    }

    public static void main(String[] args) {
        test_nextWeightIndex();
    }
}
