/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:NetUtility.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Apr 22, 2013 10:56:52 AM
 * 
 */
package com.pixshow.framework.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Apr 22, 2013
 * 
 */

public class NetUtility {
    public static void main(String[] args) {
        try {
            String domainname = "mapweather.2828.net";
            InetAddress address = InetAddress.getByName(domainname);
            System.out.println(domainname + " : " + address.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
