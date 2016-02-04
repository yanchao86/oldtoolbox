package com.pixshow.apkpack.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pixshow.framework.utils.RegexUtility;

public class ApkManifestUtils {

    public static String getMetaData(String xml, String name) {
        name = name.replaceAll("\\.", "\\\\.");
        return RegexUtility.findGroup(xml, "<meta-data" //
                + "\\s+android\\s*:\\s*name\\s*=\\s*\"(" + name + ")\"" // group 1
                + "\\s+android\\s*:\\s*value\\s*=\\s*\"([^\"]*)\"" // group 2
                + "\\s*/>", 2);
    }

    public static String replaceMetaData(String xml, String name, String value) {
        name = name.replaceAll("\\.", "\\\\.");
        Pattern pattern = Pattern.compile("<meta-data" //
                + "\\s+android\\s*:\\s*name\\s*=\\s*\"(" + name + ")\"" // group 1
                + "\\s+android\\s*:\\s*value\\s*=\\s*\"([^\"]*)\"" // group 2
                + "\\s*/>");
        Matcher matcher = pattern.matcher(xml);
        StringBuilder buf = new StringBuilder();
        int index = 0;
        while (matcher.find()) {
            buf.append(xml.substring(index, matcher.start()));
            buf.append(matcher.group().replace(matcher.group(2), value));
            index = matcher.end();
        }
        buf.append(xml.substring(index));
        buf.append(xml.substring(index));
        return buf.toString();
    }

    public static String getVersionCode(String xml) {
        return RegexUtility.findGroup(xml, "android\\s*:\\s*versionCode\\s*=\\s*\"([^\"]*)\"", 1);
    }

    public static String replaceVersionCode(String xml, String code) {
        return xml.replaceAll("android\\s*:\\s*versionCode\\s*=\\s*\"([^\"]*)\"", "android:versionCode=\"" + code + "\"");
    }

    public static String getVersionName(String xml) {
        return RegexUtility.findGroup(xml, "android\\s*:\\s*versionName\\s*=\\s*\"([^\"]*)\"", 1);
    }

    public static String replaceVersionName(String xml, String name) {
        return xml.replaceAll("android\\s*:\\s*versionName\\s*=\\s*\"([^\"]*)\"", "android:versionName=\"" + name + "\"");
    }

}
