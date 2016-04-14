package com.pixshow.toolboxmgr.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.config.Config;
import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.FileUtility;
import com.pixshow.framework.utils.HttpUtility;
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
public class ApkUploadAction extends BaseAction {
    private static final long   serialVersionUID = 1L;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    @Autowired
    private ApkUploadService    apkUploadService;
    private boolean doNotModifyFileName;
    private File                apk;
    private String              apkFileName;
    private String urlType;

    private Map<String, Object> result           = new HashMap<String, Object>();

    @Action(value = "uploadHistory", results = { @Result(name = SUCCESS, location = "/upload/apkUpload.jsp") })
    public String uploadHistory() {
        result.put("apkUploads", apkUploadService.findAll());
        return SUCCESS;
    }
    @Action(value = "apkUpload", results = { @Result(name = SUCCESS, type = "redirectAction", location = "uploadHistory") })
    public String apkUpload() {
    	
    	if(apk==null || StringUtils.isEmpty(apkFileName)){
    		return ERROR;
    	}
    	if(!apkFileName.endsWith(".apk")){
    		return ERROR;
    	}
    	if(!doNotModifyFileName){
        	String noSuffixFileName = apkFileName.substring(0,apkFileName.length()-4);
        	noSuffixFileName = noSuffixFileName.replaceAll("[^a-zA-z0-9_.]", "_");
        	apkFileName = noSuffixFileName+"_"+sdf.format(new Date())+".apk";
    	}
    	byte[] bytes =null;
		try {
			bytes = FileUtils.readFileToByteArray(apk);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		if(bytes==null){
			return ERROR;
		}
    	byte[] apkNameBytes = apkFileName.getBytes();
    	String apkSizeName = apkFileName+"_size";
    	String apkSize = bytes.length+"";
        String config = null;
        String redisConfig = null;
        JSONArray redisArr = null;
        JSONArray arr = null;
        try {
            config = FileUtility.readFileToString(new File(this.getClass().getResource("/config/config_apk_upload.properties").getFile()), "UTF-8");
            redisConfig = FileUtility.readFileToString(new File(this.getClass().getResource("/config/config_apk_upload_redis.properties").getFile()), "UTF-8");
            redisArr = JSONArray.fromObject(redisConfig);
            arr = JSONArray.fromObject(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(redisArr!=null){
            for (int i = 0; i < redisArr.size(); i++) {
                JSONObject json = redisArr.getJSONObject(i);
                String host = json.getString("host");
                int port = json.getInt("port");
                RedisFactory redisFactory = new RedisFactory(host, port);
        		Jedis jedis = redisFactory.getJedis();
                try {
        			jedis.set(apkNameBytes,bytes);
        			jedis.set(apkSizeName, apkSize);
        		} catch (Exception e1) {
        			e1.printStackTrace();
        		}finally{
        			RedisUtil.returnResource(jedis);
        		}
            }
        }
        if(arr!=null){
            List<FilePart> files = new ArrayList<FilePart>();
            try {
                files.add(new FilePart("file", apk));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < arr.size(); i++) {
                JSONObject json = arr.getJSONObject(i);
                String url = json.getString("url");
                String folder = json.getString("folder");
                Map<String, String> params = new HashMap<String, String>();
                params.put("fileName", apkFileName);
                params.put("folder", folder);
                String msg = HttpUtility.upload(url, params, files);
                json.put("result", msg);
            }
        }
        ApkUploadBean bean = new ApkUploadBean();
        bean.setMsg(arr==null?null:arr.toString());
        bean.setCreateDate(new Date());
        if("ori".equals(urlType)){
        	String oriDownloadUrlPrefix = Config.getInstance().getString("toolbox.oriDownloadUrlPrefix");
        	bean.setFileName(oriDownloadUrlPrefix+apkFileName);
        }else{
        	bean.setFileName(apkFileName);
        }
        bean.setUser("");
        apkUploadService.save(bean);
        return SUCCESS;
    }
   /* @Action(value = "apkUpload", results = { @Result(name = SUCCESS, type = "redirectAction", location = "uploadHistory") })
    public String apkUpload() {
        List<FilePart> files = new ArrayList<FilePart>();
        try {
            files.add(new FilePart("file", apk));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String config = null;
        try {
            config = FileUtility.readFileToString(new File(this.getClass().getResource("/config/config_apk_upload.properties").getFile()), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ApkUploadBean bean = new ApkUploadBean();
        JSONArray arr = JSONArray.fromObject(config);
        for (int i = 0; i < arr.size(); i++) {
            JSONObject json = arr.getJSONObject(i);
            String url = json.getString("url");
            String folder = json.getString("folder");
            Map<String, String> params = new HashMap<String, String>();
            params.put("fileName", apkFileName);
            params.put("folder", folder);
            String msg = HttpUtility.upload(url, params, files);
            json.put("result", msg);
        }
        bean.setMsg(arr.toString());
        bean.setCreateDate(new Date());
        bean.setFileName(apkFileName);
        bean.setUser("");
        apkUploadService.save(bean);

        return SUCCESS;
    }*/

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


}
