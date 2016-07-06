package com.pixshow.toolboxmgr.tools;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.pixshow.framework.utils.FileUtility;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class CheckUrlUtil {
    public JSONObject checkDownloadUrl(String downloadUrl) {
        JSONObject result = new JSONObject();
        String d1 = "http://balance2.chinacloudapp.cn";
        String d2 = "http://apk.idotools.com";
        if (downloadUrl.indexOf(d1) < 0 || downloadUrl.indexOf(d2) < 0) {
            return result;
        }
        if (StringUtils.isEmpty(downloadUrl)) {
            result.put("error", "url is null");
            return result;
        }
        String[] arr = downloadUrl.split("/");

        String rKey = arr[arr.length - 1];

        String redisConfig = null;
        JSONArray redisArr = new JSONArray();
        try {
            redisConfig = FileUtility.readFileToString(new File(this.getClass().getResource("/config/config_apk_upload_redis.properties").getFile()), "UTF-8");
            redisArr = JSONArray.fromObject(redisConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray nodes = new JSONArray();
        for (int i = 0; i < redisArr.size(); i++) {
            JSONObject json = redisArr.getJSONObject(i);
            String host = json.getString("host");
            int port = json.getInt("port");
            RedisFactory redisFactory = new RedisFactory(host, port);
            Jedis jedis = redisFactory.getJedis();
            try {
                //                jedis.get(rKey.getBytes());
                boolean exist = jedis.exists(rKey + "_size");
                if (exist) {
                    int size = Integer.parseInt(jedis.get(rKey + "_size"));
                    if (size < 1024) {
                        nodes.add(host + " - size=" + size);
                    }
                } else {
                    result.put("error", "File error !");
                    nodes.add("On [" + host + "] does not exist");
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                RedisUtil.returnResource(jedis);
            }
        }
        result.put("nodes", nodes);
        return result;
    }

    public static void main(String[] args) {
        String downloadUrl = "http://apk.idotools.com/com.racergame.cityracing3d.dt_feiche.apk";

        String[] arr = downloadUrl.split("/");
        String d1 = "http://balance2.chinacloudapp.cn";
        String d2 = "http://apk.idotools.com";

        if (downloadUrl.indexOf(d1) < 0 || downloadUrl.indexOf(d2) < 0) {
            System.out.println("sssssssssssss");        
        }
        String rKey = arr[arr.length - 1];
        System.out.println(rKey);
        
        JSONObject rs = new CheckUrlUtil().checkDownloadUrl(downloadUrl);
        System.out.println(rs.toString());
    }
}
