/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:AbbreviationProcessorTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Feb 26, 2013 3:19:49 PM
 * 
 */
package com.pixshow.framework.abbr.internal;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Feb 26, 2013
 * 
 */

public class AbbreviationProcessorTests {
    public static void main(String[] args) {
        String[] words = new String[] { "description", "group", "friend" };
        for (String word : words) {
            System.out.println(word + " = " + AbbreviationProcessor.abbr("description"));
        }
    }
}
