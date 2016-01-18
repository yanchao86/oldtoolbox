/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:Config.java Project: Framework
 * 
 * Creator:JFL 
 * Date:2012-5-16 下午04:19:05
 * 
 */
package com.pixshow.framework.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;

import com.pixshow.framework.exception.api.SysException;
import com.pixshow.framework.utils.FileUtility;
import com.pixshow.framework.utils.StringUtility;

/**
 * 
 * 
 * 
 * @author JFL
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.4 $ $Date: 2012/10/09 02:01:29 $
 * @since 2012-5-16
 * 
 */

public class Config {
    private static final Log          log         = LogFactory.getLog(Config.class);
    private static final String       CONFIG_FILE = "classpath:config/config*.properties";
    private final Map<String, String> configCache = new HashMap<String, String>();
    private static final Config       instance    = new Config();

    private Config() {
        Resource[] resources = FileUtility.getResources(CONFIG_FILE);
        for (Resource resource : resources) {

            InputStream in = null;
            try {
                Properties properties = new Properties();
                in = resource.getInputStream();
                properties.load(in);
                Enumeration<?> propertyNames = properties.propertyNames();
                while (propertyNames != null && propertyNames.hasMoreElements()) {
                    String name = (String) propertyNames.nextElement();
                    if (StringUtils.isNotEmpty(name)) {
                        configCache.put(name, properties.getProperty(name, ""));
                    }
                }
            } catch (Exception e) {
                log.error("解析配置文件异常(" + resource.getFilename() + ")->", e);
                throw new SysException(e);
            } finally {
                IOUtils.closeQuietly(in);
            }
        }
    }

    public static Config getInstance() {
        return instance;
    }

    public List<String> getKeys() {
        return new ArrayList<String>(configCache.keySet());
    }

    public List<String> getKeys(String regex) {
        List<String> matchedKeys = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regex);
        for (String key : configCache.keySet()) {
            if (pattern.matcher(key).matches()) {
                matchedKeys.add(key);
            }
        }
        return matchedKeys;
    }

    public Set<String> getKeys(String regex, int group) {
        Set<String> matchedKeys = new TreeSet<String>();
        Pattern pattern = Pattern.compile(regex);
        for (String key : configCache.keySet()) {
            Matcher matcher = pattern.matcher(key);
            if (matcher.matches()) {
                matchedKeys.add(matcher.group(group));
            }
        }
        return matchedKeys;
    }

    public String getString(String key) {
        return configCache.get(key);
    }

    public String getString(String key, String defaultValue) {
        String v = getString(key);
        return v == null ? defaultValue : v;
    }

    public Boolean getBoolean(String key) {
        String v = getString(key);
        if (StringUtility.isBoolean(v)) {
            return Boolean.valueOf(v);
        }
        return null;
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        Boolean v = getBoolean(key);
        return v == null ? defaultValue : v;
    }

    public Integer getInteger(String key) {
        String v = getString(key);
        if (StringUtility.isNumeric(v)) {
            return Integer.valueOf(v);
        }
        return null;
    }

    public Integer getInteger(String key, Integer defaultValue) {
        Integer v = getInteger(key);
        return v == null ? defaultValue : v;
    }

}
