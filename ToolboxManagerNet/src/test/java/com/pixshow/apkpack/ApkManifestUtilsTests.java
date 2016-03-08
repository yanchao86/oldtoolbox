package com.pixshow.apkpack;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.pixshow.apkpack.utils.ApkManifestUtils;

public class ApkManifestUtilsTests {
    public static void main(String[] args) throws Exception {
        String xml = FileUtils.readFileToString(new File("C:/Users/JFL/Desktop/打包/AndroidManifest.xml"), "UTF-8");
        String newxml = ApkManifestUtils.replaceMetaData(xml, "UMENG_APPKEY", "000000000000000000000000");
        newxml = ApkManifestUtils.replaceVersionCode(xml, "111");
        newxml = ApkManifestUtils.replaceVersionName(xml, "111");

        System.out.println(newxml);
    }
}
