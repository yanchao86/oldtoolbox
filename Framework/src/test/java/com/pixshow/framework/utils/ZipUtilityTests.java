/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:ZipUtility.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Dec 19, 2013 7:17:35 PM
 * 
 */
package com.pixshow.framework.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.IOUtils;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Dec 19, 2013
 * 
 */

public class ZipUtilityTests {

    public static void main(String[] args) {
        File file = FileUtility.createTempDir("zip");
        System.out.println(file.getAbsolutePath());
    }

    public static void main1(String[] args) throws Exception {
        ZipFile zipFile = new ZipFile("F:/图片/wallpaper/wallpaper.zip");
        Enumeration<ZipArchiveEntry> enumeration = zipFile.getEntries();
        while (enumeration.hasMoreElements()) {
            ZipArchiveEntry entry = enumeration.nextElement();

            String fileName = new String(entry.getRawName(), "UTF-8");

            System.out.println(fileName);

            if (!fileName.endsWith("/")) {
                InputStream io = zipFile.getInputStream(entry);
                File outFile = new File("F:/图片/wallpaper/wallpaper/" + fileName);
                outFile.getParentFile().mkdirs();
                IOUtils.copy(io, new FileOutputStream(outFile));
            }
        }
    }
}
