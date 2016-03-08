package com.pixshow;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.pixshow.framework.utils.HttpUtility;


public class Test {

    
    public static void main(String[] args) {
        try {
            String url = "http://127.0.0.1:8080/ToolboxService/service/customGridSql.do?code=app_review_config";
            Map<String, String> params = new HashMap<String, String>();
            params.put("sql", URLEncoder.encode("app_packge.name=com.ejnet.weathercamera and app_packge.appName=天气相机", "UTF-8"));
            System.out.println(HttpUtility.post(url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
