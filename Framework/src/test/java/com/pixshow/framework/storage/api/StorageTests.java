/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:StorageTests.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Nov 13, 2013 11:27:09 AM
 * 
 */
package com.pixshow.framework.storage.api;

import java.io.File;
import java.util.UUID;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Nov 13, 2013
 * 
 */

public class StorageTests {

    public static void test_upload(String name) {
        Storage storage = StorageManager.getStorage();
        String fileName = "/weatherphoto/userface/" + name + ".jpg";
        File file = new File("C:/Users/JFL/Desktop/头像匹配/头像/1.jpg");
        String url = storage.upload("vimg", fileName, file, "image/jpeg");
        System.out.println(url);
    }

    public static void test_uploadAsync(String name) {
        Storage storage = StorageManager.getStorage();
        String fileName = "/weatherphoto/userface/" + name + ".jpg";
        File file = new File("C:/Users/JFL/Desktop/头像匹配/头像/1.jpg");
        String url = storage.uploadAsync("vimg", fileName, file, "image/jpeg");
        System.out.println(url);
    }

    public static void main(String[] args) {
        //        test_upload(UUID.randomUUID().toString());

        for (int i = 0; i < 100; i++) {
            test_uploadAsync("name-" + i);
        }
    }
}
