package com.pixshow.toolboxmgr.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.config.Config;
import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.DigestUtility;
import com.pixshow.framework.utils.FileUtility;
import com.pixshow.framework.utils.HttpUtility;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.bean.ApkUploadBean;
import com.pixshow.toolboxmgr.service.ApkUploadService;
import com.pixshow.toolboxmgr.tools.AliyunTool;
import com.pixshow.toolboxmgr.tools.ApkTool;
import com.pixshow.toolboxmgr.tools.RedisFactory;
import com.pixshow.toolboxmgr.tools.RedisUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

@Controller
@Scope("prototype")
@Namespace("/manager/new")
public class ApkUploadActionNew extends BaseAction {
    private static final long serialVersionUID = 1L;
    @Autowired
    private ApkUploadService  apkUploadService;
    private boolean           doNotModifyFileName;
    private File              apk;
    private String            apkFileName;
    private String            urlType;
    private String            channel;

    private Map<String, Object> result = new HashMap<String, Object>();

    @Action(value = "uploadHistory", results = { @Result(name = SUCCESS, location = "/upload/apkUploadnew.jsp") })
    public String uploadHistory() {
        result.put("apkUploads", apkUploadService.findAll());
        return SUCCESS;
    }

    @Action(value = "apkUpload", results = { @Result(name = SUCCESS, type = "redirectAction", location = "uploadHistory") })
    public String apkUpload() {
        if (apk == null || !apk.exists() || apk.length() == 0) {
            return ERROR;
        }
        if (StringUtility.isEmpty(channel)) {
            return ERROR;
        }
        byte[] bytes = null;
        String md5 = null;
        String SHA1 = null;
        try {
            bytes = FileUtils.readFileToByteArray(apk);
            md5 = DigestUtility.md5Hex(bytes);
            SHA1 = FileUtility.sha1(apk);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bytes == null) {
            return ERROR;
        }
        String packageName = ApkTool.parseAPK(apk).getString("packageName");

        String config = null;
        String redisConfig = null;
        JSONArray redisArr = new JSONArray();
        JSONArray uploadArr = null;
        try {
            config = FileUtility.readFileToString(new File(this.getClass().getResource("/config/config_apk_upload.properties").getFile()), "UTF-8");
            redisConfig = FileUtility.readFileToString(new File(this.getClass().getResource("/config/config_apk_upload_redis.properties").getFile()), "UTF-8");
            redisArr = JSONArray.fromObject(redisConfig);
            uploadArr = JSONArray.fromObject(config);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String rKey = packageName + "_" + channel + ".apk";

        for (int i = 0; i < redisArr.size(); i++) {
            JSONObject json = redisArr.getJSONObject(i);
            String host = json.getString("host");
            int port = json.getInt("port");
            RedisFactory redisFactory = new RedisFactory(host, port);
            Jedis jedis = redisFactory.getJedis();
            try {
                jedis.set(rKey.getBytes(), bytes);
                jedis.set(rKey + "_size", apk.length() + "");
                jedis.set(rKey + "_sha1", SHA1);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                RedisUtil.returnResource(jedis);
            }
        }

        try {
            List<FilePart> files = new ArrayList<FilePart>();
            files.add(new FilePart("file", apk));
            for (int i = 0; i < uploadArr.size(); i++) {
                JSONObject json = uploadArr.getJSONObject(i);
                String url = json.getString("url");
                String folder = json.getString("folder");
                Map<String, String> params = new HashMap<String, String>();
                params.put("fileName", rKey);
                params.put("folder", folder);
                String msg = HttpUtility.upload(url, params, files);
                json.put("result", msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApkUploadBean bean = new ApkUploadBean();
        bean.setMd5(md5);
        bean.setSha1(SHA1);
        bean.setMsg(uploadArr == null ? null : uploadArr.toString());
        bean.setCreateDate(new Date());
        if ("ori".equals(urlType)) {
            String oriDownloadUrlPrefix = Config.getInstance().getString("toolbox.oriDownloadUrlPrefix");
            bean.setFileName(oriDownloadUrlPrefix + rKey);
        } else {
            bean.setFileName(rKey);
            //刷新CDN缓存
            AliyunTool.refreshCDN("File", Config.getInstance().getString("toolbox.apkDownloadUrl") + rKey);
        }
        bean.setUser("");
        apkUploadService.save(bean);
        return SUCCESS;
    }

    public File getApk() {
        return apk;
    }

    public void setApk(File apk) {
        this.apk = apk;
    }

    public String getApkFileName() {
        return apkFileName;
    }

    public void setApkFileName(String apkFileName) {
        this.apkFileName = apkFileName;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public String getUrlType() {
        return urlType;
    }

    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }

    public boolean isDoNotModifyFileName() {
        return doNotModifyFileName;
    }

    public void setDoNotModifyFileName(boolean doNotModifyFileName) {
        this.doNotModifyFileName = doNotModifyFileName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

}
