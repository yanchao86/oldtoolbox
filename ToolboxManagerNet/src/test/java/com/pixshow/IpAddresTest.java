package com.pixshow;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

public class IpAddresTest {

    public static void main(String[] args) {
        HttpServletRequest request = ServletActionContext.getRequest();
        String ipAddress = "";
        try {
            ipAddress = request.getHeader("X-FORWARDED-FOR");
            if(ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(ipAddress != null && !"".equals(ipAddress)) {
            int m = ipAddress.lastIndexOf(",");
            ipAddress = ipAddress.substring(m+1).trim();
        }
        System.out.println(ipAddress);
    }
}
