package com.pixshow.toolboxmgr.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.FileUtility;
import com.pixshow.toolboxmgr.bean.ApkUploadBean;
import com.pixshow.toolboxmgr.service.ApkUploadService;
import com.pixshow.toolboxmgr.tools.RedisFactory;
import com.pixshow.toolboxmgr.tools.RedisUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class Sha1ToRedisAction extends BaseAction {
    private static final long serialVersionUID = 1L;
    @Autowired
    private ApkUploadService  apkUploadService;

    /**
    set com.gcol.kkkwan_maoni.apk_sha1 [sha1值字符串]
    如:
    set com.gcol.kkkwan_maoni.apk_sha1 1b3c1b3c1b2c3b12c3b
     */
    private JSONObject result = new JSONObject();

    @Action(value = "sha1", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String sha1() {
        String redisConfig = null;
        JSONArray redisArr = new JSONArray();
        try {
            redisConfig = FileUtility.readFileToString(new File(this.getClass().getResource("/config/config_apk_upload_redis.properties").getFile()), "UTF-8");
            redisArr = JSONArray.fromObject(redisConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<ApkUploadBean> apks = apkUploadService.findsSha1();

        JSONArray rs = new JSONArray();
        for (ApkUploadBean apk : apks) {
            String pg = apk.getFileName();
            String sha1 = apk.getSha1();
            //http://apk.cat429.com/
            //http://balance2.chinacloudapp.cn/
            pg = pg.replace("http://apk.cat429.com/", "").replace("http://balance2.chinacloudapp.cn/", "");
            String rKey = pg + "_sha1";
            for (int i = 0; i < redisArr.size(); i++) {
                JSONObject json = redisArr.getJSONObject(i);
                String host = json.getString("host");
                int port = json.getInt("port");
                RedisFactory redisFactory = new RedisFactory(host, port);
                Jedis jedis = redisFactory.getJedis();
                try {
                    jedis.set(rKey, sha1);
                    JSONObject r = new JSONObject();
                    r.put(rKey, sha1);
                    rs.add(r);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    RedisUtil.returnResource(jedis);
                }
            }
        }
        result.put("to redis data", rs);
        return SUCCESS;
    }

    public static void main(String[] args) {
        String pg = "http://balance2.chinacloudapp.cn/aaaa";
        ;
        pg = pg.replace("http://apk.cat429.com/", "").replace("http://balance2.chinacloudapp.cn/", "");
        System.out.println(pg);
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

}
