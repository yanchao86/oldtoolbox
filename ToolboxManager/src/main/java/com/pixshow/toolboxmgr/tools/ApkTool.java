package com.pixshow.toolboxmgr.tools;

import java.io.File;
import java.util.Locale;

import net.dongliu.apk.parser.ApkParser;
import net.dongliu.apk.parser.bean.ApkMeta;
import net.sf.json.JSONObject;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class ApkTool {

    public static JSONObject parseAPK(File apkFile) {
        JSONObject result = new JSONObject();
        try {
            String fileName = apkFile.getName();
            result.put("fileName", fileName);
            result.put("FileSize", apkFile.length());

            ApkParser apkParser = new ApkParser(apkFile);
            apkParser.setPreferredLocale(Locale.SIMPLIFIED_CHINESE);
            ApkMeta apkMeta = apkParser.getApkMeta();
            String packageName = apkMeta.getPackageName();
            result.put("packageName", packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
