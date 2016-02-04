/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:PermissionTools.java Project: LvWeatherPushService
 * 
 * Creator:4399-lvtu-8 
 * Date:Oct 24, 2013 6:37:30 PM
 * 
 */
package com.pixshow.login.tools;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Oct 24, 2013
 * 
 */

public class PermissionTool {

    public static boolean validate(HttpSession session, String fnName) {
        Object obj = session.getAttribute("rightPermission");
        if(obj == null) {
            return false;
        }
        String[] per = obj.toString().split(",");
        List<String> pers = Arrays.asList(per);
        if(pers.contains(fnName)) {
            return true;
        } else {
            return false;
        }
    }
    
}
