/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:SqlUtility.java Project: LvFramework
 * 
 * Creator:4399-lvtu-8 
 * Date:Apr 18, 2013 7:13:38 PM
 * 
 */
package com.pixshow.framework.utils;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Apr 18, 2013
 * 
 */

public class SqlUtility {
    
    // list > in
    public static String in(String fieldName, int count) {
        if(count == 0) {
            return "";
        }
        StringBuilder sql = new StringBuilder(fieldName + " in (");
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        return sql.append(")").toString();
    }
    //

    // list > in
    public static String notIn(String fieldName, int count) {
        if(count == 0) {
            return "";
        }
        StringBuilder sql = new StringBuilder(fieldName + " not in (");
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        return sql.append(")").toString();
    }
    //
}
