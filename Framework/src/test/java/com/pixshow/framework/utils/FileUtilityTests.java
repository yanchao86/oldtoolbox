/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:FileUtilityTests.java Project: LvFramework
 * 
 * Creator:4399-lvtu-8 
 * Date:Mar 28, 2013 3:26:27 PM
 * 
 */
package com.pixshow.framework.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Mar 28, 2013
 * 
 */

public class FileUtilityTests {

    public static void test_zip() throws Exception {
        List<File> files = new ArrayList<File>();
        files.add(new File("F:/androidpack/game2.apk"));
        files.add(new File("F:/androidpack/game2_new.apk"));
        files.add(new File("F:/androidpack/game2_newsign.apk"));

        FileUtility.zip(files, new File("F:/androidpack/test.zip"));

    }

    public static void main(String[] args) throws Exception {
        test_zip();
    }
}
