/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:I18NUtility.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:May 7, 2013 10:18:03 AM
 * 
 */
package com.pixshow.framework.i18n.api;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;

import com.pixshow.framework.utils.StringUtility;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since May 7, 2013
 * 
 */

public class TextManager {

    private static final String                         DEFAULT_ENCODING = "UTF-8";
    private static Map<String, PropertiesConfiguration> cache            = new HashMap<String, PropertiesConfiguration>();
    private static Pattern                              pattern          = Pattern.compile("\\$\\{\\w+\\}");

    private static String replace(String text, Map<String, Object> parms) {
        Matcher matcher = pattern.matcher(text);
        int beginIndex = 0;
        StringBuilder buf = new StringBuilder();
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            buf.append(text.substring(beginIndex, start));

            Object value = parms.get(text.substring(start + 2, end - 1));
            buf.append(value == null ? "" : value.toString());
            beginIndex = end;
        }
        buf.append(text.substring(beginIndex));
        return buf.toString();
    }

    public static String getString(String name, String key, String local, Map<String, Object> parms) {
        return replace(getString(name, key, local), parms);
    }

    public static String getString(String name, String key, Map<String, Object> parms) {
        return replace(getString(name, key), parms);
    }

    public static String getString(String name, String key, String local) {
        PropertiesConfiguration configuration = findLanguage(name, local, DEFAULT_ENCODING);
        if (configuration == null) {
            return null;
        }
        return configuration.getString(key);
    }

    public static String getString(String name, String key) {
        return getString(name, key, "");
    }

    private static PropertiesConfiguration initConfiguration(String key) {
        try {
            URL url = TextManager.class.getResource("/i18n/" + key + ".properties");
            if (url != null) {
                File file = new File(url.toURI());
                if (file.exists()) {
                    PropertiesConfiguration configuration = new PropertiesConfiguration();
                    configuration.setEncoding("UTF-8");
                    configuration.load(file);
                    return configuration;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static PropertiesConfiguration findConfiguration(String fileName, String encoding) {
        PropertiesConfiguration configuration = cache.get(fileName);
        if (configuration == null) {
            synchronized (cache) {
                configuration = initConfiguration(fileName);
                if (configuration == null) {
                    configuration = new EmptyPropertiesConfiguration();
                }
                cache.put(fileName, configuration);
            }
        }
        return configuration;
    }

    private static PropertiesConfiguration findLanguage(String name, String local, String encoding) {

        local = local.toLowerCase();

        if (StringUtils.isEmpty(local)) { // 默认语言
            PropertiesConfiguration configuration = findConfiguration(name, encoding);
            return configuration;
        }

        PropertiesConfiguration configuration = findConfiguration(name + "_" + local, encoding);

        if (configuration instanceof EmptyPropertiesConfiguration) {
            String[] langCodes = StringUtility.split(local, "_");
            String fileName = name + "_" + langCodes[0];
            configuration = findConfiguration(fileName, encoding);
        }

        if (configuration instanceof EmptyPropertiesConfiguration) {// 默认语言
            configuration = findConfiguration(name, encoding);
        }
        // 获取默认配置文件
        return configuration;
    }

    private static class EmptyPropertiesConfiguration extends PropertiesConfiguration {
    }

}
