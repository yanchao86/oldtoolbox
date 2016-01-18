/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:CompositeKey.java Project: LvPhotoService_1119
 * 
 * Creator:4399-lvtu-8 
 * Date:Apr 16, 2013 3:02:43 PM
 * 
 */
package com.pixshow.framework.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Apr 16, 2013
 * 
 */

public class CompositeKey {

    public static class Separator {
        private String separator;

        private Separator(String separator) {
            this.separator = separator;
        }
    }

    public static Separator sep(String separator) {
        return new Separator(separator);
    }

    private List<Object> keyList   = new ArrayList<Object>();

    private Separator    separator = new Separator("-");

    public CompositeKey(Object... keys) {
        if(keys == null) {
            return;
        }
        for (Object key : keys) {
            keyList.add(key);
        }
    }

    public CompositeKey(Separator separator, Object... keys) {
        if(keys == null) {
            return;
        }
        for (Object key : keys) {
            keyList.add(key);
        }
        this.separator = separator;
    }

    public void addKey(Object key) {
        keyList.add(key);
    }

    public String getKey() {
        StringBuilder buf = new StringBuilder();
        for (Object key : keyList) {
            if (buf.length() > 0) {
                buf.append(separator.separator);
            }
            buf.append(key.toString());

        }
        return buf.toString();
    }

    public Object getKey(int index) {
        return keyList.get(index);
    }

    public String getId() {
        return getKey();
    }

    public Object getKey1() {
        return getKey(0);
    }

    public Object getKey2() {
        return getKey(1);
    }

    public Object getKey3() {
        return getKey(3);
    }

    @Override
    public String toString() {
        return getKey();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof CompositeKey))
            return false;
        if (((CompositeKey) obj).getKey().equals(this.getKey())) {
            return true;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }
}
