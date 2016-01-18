/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:AbbreviationProcesser.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jan 18, 2013 4:25:58 PM
 * 
 */
package com.pixshow.framework.abbr.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pixshow.framework.config.Config;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Jan 18, 2013
 * 
 */

public class AbbreviationProcessor {

    private static Map<String, String> addrCache   = new HashMap<String, String>();
    private static Map<String, String> addrMapping = new HashMap<String, String>();

    private static final Pattern       pattern     = Pattern.compile("(abbr\\.)(\\w+)"); //abbr.xxxx=xxxxx
    static {
        List<String> keys = Config.getInstance().getKeys();
        for (String key : keys) {
            Matcher matcher = pattern.matcher(key);
            if (matcher.matches()) {
                addrMapping.put(Config.getInstance().getString(key), matcher.group(2));
            }
        }
    }

    private static String firstLetterToUpper(String str) {
        char[] array = str.toCharArray();
        array[0] = Character.toUpperCase(array[0]);
        return String.valueOf(array);
    }

    private static String[] getWords(String name) {
        if (name.length() <= 1) {
            return new String[] { name };
        }
        char[] chars = name.toCharArray();
        int[] flag = new int[chars.length];
        int wordCount = 1;
        for (int i = 0; i < chars.length; i++) {
            if (i != 0 && Character.isUpperCase(chars[i])) {
                flag[wordCount++] = i;
            }
            chars[i] = Character.toLowerCase(chars[i]);
        }
        flag[wordCount] = chars.length;

        String[] words = new String[wordCount];
        for (int i = 0; i < words.length; i++) {
            words[i] = new String(chars, flag[i], flag[i + 1] - flag[i]);
        }

        return words;
    }

    public static String abbr(String name) {
        String abbr = addrCache.get(name);
        if (abbr == null) {
            StringBuilder buf = new StringBuilder();
            String[] words = getWords(name);
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                String addr = addrMapping.get(word);
                if (addr != null) {
                    word = addr;
                }
                if (i != 0) {
                    word = firstLetterToUpper(word);
                }
                buf.append(word);
            }
            abbr = buf.toString();
            synchronized (addrCache) {
                addrCache.put(name, abbr);
            }
        }

        return abbr;
    }

}
