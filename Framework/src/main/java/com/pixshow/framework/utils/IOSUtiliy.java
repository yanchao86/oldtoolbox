/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:Emoji.java Project: TestWeb
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Apr 8, 2013 4:34:13 PM
 * 
 */
package com.pixshow.framework.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javapns.Push;
import javapns.notification.PayloadPerDevice;
import javapns.notification.PushNotificationPayload;

import org.apache.commons.io.IOUtils;

import com.pixshow.framework.utils.RegexUtility.ReplaceProcessor;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Apr 8, 2013
 * 
 */

public class IOSUtiliy {

    private static final String regex = "\\$\\{U\\+(\\w)+\\}"; //${U+XXXXX}

    public static String emoticonReplace(final String text) {
        return RegexUtility.replace(text, regex, new ReplaceProcessor() {
            @Override
            public String replace(String str) {
                Integer v = Integer.valueOf(str.substring("${U+".length(), str.length() - 1), 16);
                return CharacterUtility.codePointToUTF16(v);
            }
        });
    }

    private static class ApnsKey {
        String password;
        File   keyFile;
    }

    public static class PN {

        private String              alert;
        private int                 badge;
        private String              sound;
        private Map<String, String> customMap;
        private String              token;

        public PN(String alert, int badge, String sound, Map<String, String> customMap, String token) {
            this.alert = alert;
            this.badge = badge;
            this.sound = sound;
            this.customMap = customMap;
            this.token = token;
        }

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        public int getBadge() {
            return badge;
        }

        public void setBadge(int badge) {
            this.badge = badge;
        }

        public String getSound() {
            return sound;
        }

        public void setSound(String sound) {
            this.sound = sound;
        }

        public Map<String, String> getCustomMap() {
            return customMap;
        }

        public void setCustomMap(Map<String, String> customMap) {
            this.customMap = customMap;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

    }

    private static Map<String, ApnsKey> keysStore = new HashMap<String, ApnsKey>();

    private static ApnsKey getApnsKey(String keyName) {
        ApnsKey key = keysStore.get(keyName);
        if (key == null) {
            try {
                File keyFile = new File(IOSUtiliy.class.getResource("/apnskeys/" + keyName + ".p12").toURI());
                String password = IOUtils.toString(IOSUtiliy.class.getResource("/apnskeys/" + keyName + ".pwd")).trim();
                //System.out.println("app push>>" + keyFile.getAbsolutePath() + " > " + keyFile.exists() + " > " + keyFile.length() + "  password=" + password);
                key = new ApnsKey();
                key.keyFile = keyFile;
                key.password = password;
                synchronized (keysStore) {
                    keysStore.put(keyName, key);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return key;
    }

    public static void push(String keyName, int badge, String token) {
        push(keyName, null, badge, null, null, token);
    }

    public static void push(String keyName, String alert, int badge, String token) {
        push(keyName, alert, badge, null, null, token);
    }

    public static void push(String keyName, String alert, int badge, String sound, String token) {
        push(keyName, alert, badge, sound, null, token);
    }

    public static void push(String keyName, String alert, int badge, String sound, Map<String, String> customMap, String token) {
        List<PN> pns = new ArrayList<PN>();
        pns.add(new PN(alert, badge, sound, customMap, token));
        pushAll(keyName, pns);
    }

    public static boolean pushAll(String keyName, List<PN> pns) {
        ApnsKey apnsKey = getApnsKey(keyName);
        if (apnsKey == null) {
            return false;
        }

        List<PayloadPerDevice> payloadDevicePairs = new ArrayList<PayloadPerDevice>();
        try {
            for (PN pn : pns) {
                PushNotificationPayload custom = new PushNotificationPayload();
                Map<String, String> customMap = pn.getCustomMap();
                if (customMap != null) {
                    for (String key : customMap.keySet()) {
                        custom.addCustomDictionary(key, customMap.get(key));
                    }
                }
                if (pn.getAlert() != null) {
                    custom.addAlert(pn.getAlert());
                }
                if (pn.getBadge() != 0) {
                    custom.addBadge(pn.getBadge());
                }
                if (pn.getSound() != null) {
                    custom.addSound(pn.getSound());
                }
                payloadDevicePairs.add(new PayloadPerDevice(custom, pn.getToken()));
            }
            if (payloadDevicePairs.size() > 0) {
                Push.payloads(apnsKey.keyFile, apnsKey.password, true, payloadDevicePairs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

}
