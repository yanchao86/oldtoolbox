package com.pixshow.toolboxmgr.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.config.Config;
import com.pixshow.framework.support.BaseAction;
import com.pixshow.toolboxmgr.bean.ApkUploadBean;
import com.pixshow.toolboxmgr.service.ApkUploadService;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class ApkUploadAction extends BaseAction {
    private static final long             serialVersionUID = 1L;
    private static final SimpleDateFormat sdf              = new SimpleDateFormat("yyyyMMddHHmmss");
    @Autowired
    private ApkUploadService              apkUploadService;
    private boolean                       doNotModifyFileName;
    private File                          apk;
    private String                        apkFileName;
    private String                        urlType;

    private Map<String, Object> result = new HashMap<String, Object>();

    @Action(value = "uploadHistory", results = { @Result(name = SUCCESS, location = "/upload/apkUpload.jsp") })
    public String uploadHistory() {
        result.put("apkUploads", apkUploadService.findAll());
        return SUCCESS;
    }

    @Action(value = "apkUpload", results = { @Result(name = SUCCESS, type = "redirectAction", location = "uploadHistory") })
    public String apkUpload() {

        if (apk == null || StringUtils.isEmpty(apkFileName)) {
            return ERROR;
        }
        if (!apkFileName.endsWith(".apk")) {
            return ERROR;
        }
        if (!doNotModifyFileName) {
            String noSuffixFileName = apkFileName.substring(0, apkFileName.length() - 4);
            noSuffixFileName = noSuffixFileName.replaceAll("[^a-zA-z0-9_.]", "_");
            apkFileName = noSuffixFileName + "_" + sdf.format(new Date()) + ".apk";
        }

//        ImageStorageTootl.upload(apkFileName, apk);
        
        String uploadPath = Config.getInstance().getString("toolbox.upload.base.folder") + Config.getInstance().getString("toolbox.img.folder") + apkFileName;
        apk.renameTo(new File(uploadPath));
        
        ApkUploadBean bean = new ApkUploadBean();
        bean.setCreateDate(new Date());
        bean.setFileName(apkFileName);
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
