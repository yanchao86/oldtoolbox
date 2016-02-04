package com.pixshow.apkpack;

import com.pixshow.apkpack.utils.ApkSignUtils;


public class ApkSignUtilsTests {
    public static void main(String[] args) {
        String root = "C:/ApkPack/";
        String publicKeyFileName = root + "dotoolsTP.x509.pem";
        String privateKeyFileName = root + "dotoolsTP.pk8";
        String inputJarFileName = root + "game2.jar";
        String outputFileName = root + "game2_new.jar";
        ApkSignUtils.sign(publicKeyFileName, privateKeyFileName, inputJarFileName, outputFileName, false, null);
    }
}
