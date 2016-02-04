package com.pixshow.apkpack.utils;

import javax.servlet.http.HttpServletRequest;

public class IpAddressUtility {

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = "";
        try {
            ipAddress = request.getHeader("X-Forwarded-For");
//            ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (ipAddress != null && !"".equals(ipAddress)) {
            int m = ipAddress.lastIndexOf(",");
            ipAddress = ipAddress.substring(m + 1).trim();
        }
        return ipAddress;
    }
}
