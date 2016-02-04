package com.pixshow.apkpack;

import java.io.File;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.pixshow.apkpack.utils.ApkManifestUtils;
import com.pixshow.apkpack.utils.ApkSignUtils;
import com.pixshow.apkpack.utils.ApkUtils;


/**
 * http://mgr-oss.tv163.com/upload.jsp?scerectKey=DSJK164dsaDLD
 * @author 4399-lvtu-8
 *
 */
public class ApkUtilsTests {
    public static void main(String[] args) throws Exception {
        String root = "C:/ApkPack/";
        String apk = root + "FlashLight_1.6.1.apk";
        String newApk = root + "FlashLight_1.6.1_new.apk";
        String newsignApk = root + "FlashLight_1.6.1_newsign.apk";
        String buildDir = root + "FlashLight_1.6.1";
        String androidManifest = buildDir + "/" + "AndroidManifest.xml";

        String publicKeyFileName = root + "17sy-keystore.x509.pem";
        String privateKeyFileName = root + "17sy-keystore.pk8";

        // 删除临时目录
        FileUtils.deleteDirectory(new File(buildDir));

        // 解压APK
        ApkUtils.decode(apk, buildDir);

        // 修改项目了文件
        String androidManifestXml = FileUtils.readFileToString(new File(androidManifest), "UTF-8");
        // 修改versioncode
        androidManifestXml = ApkManifestUtils.replaceVersionCode(androidManifestXml, "" + System.currentTimeMillis() / 1000);
        // 修改versionname
        androidManifestXml = ApkManifestUtils.replaceVersionName(androidManifestXml, "" + DateFormatUtils.format(new Date(), "yyyyMMdd.HHmmss"));
        // 修改channel
        androidManifestXml = ApkManifestUtils.replaceMetaData(androidManifestXml, "UNMEN_KEY", "444444444444444");
        // 写入xml
        FileUtils.write(new File(androidManifest), androidManifestXml, "UTF-8");

        // 打包APK
        ApkUtils.build(buildDir, newApk);

        // 对APK进行签名
        ApkSignUtils.sign(publicKeyFileName, privateKeyFileName, newApk, newsignApk, false, null);

        // 删除临时目录
        // FileUtils.deleteDirectory(new File(buildDir));
    }
    
    
    
    
}
