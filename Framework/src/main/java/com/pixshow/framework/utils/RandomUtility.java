/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:RandomUtility.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Nov 6, 2013 6:00:42 PM
 * 
 */
package com.pixshow.framework.utils;

import org.apache.commons.lang.math.RandomUtils;

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

public class RandomUtility extends RandomUtils {
    //weightRandom
    public static int nextWeightIndex(int... weights) {
        int total = 0;
        for (int i = 0; i < weights.length; i++) {
            total += weights[i];
        }
        int random = nextInt(total);
        int regionMin = 0;
        int regionMax = 0;
        for (int i = 0; i < weights.length; i++) {
            regionMin = regionMax;
            regionMax += weights[i];
            if (regionMin <= random && random < regionMax) {
                return i;
            }
        }
        return 0;
    }
}
