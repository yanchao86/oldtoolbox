package com.pixshow.apkpack.action;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.pixshow.apkpack.bean.ApkKeyBean;
import com.pixshow.apkpack.bean.ApkProductBean;
import com.pixshow.apkpack.bean.ApkSignBean;
import com.pixshow.apkpack.service.ApkChannelService;
import com.pixshow.apkpack.service.ApkKeyService;
import com.pixshow.apkpack.service.ApkProductService;
import com.pixshow.apkpack.service.ApkSignService;
import com.pixshow.apkpack.utils.ApkSignWorker;
import com.pixshow.apkpack.utils.IpAddressUtility;
import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.FileUtility;
import com.pixshow.framework.utils.UUIDUtility;
import com.pixshow.toolboxmgr.tools.ImageStorageTootl;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class ApkSignAction extends BaseAction {
    private static final long   serialVersionUID = 1L;
    @Autowired
    private ApkSignService      apkSignService;
    @Autowired
    private ApkProductService   apkProductService;
    @Autowired
    private ApkKeyService       apkKeyService;
    @Autowired
    private ApkChannelService   apkChannelService;

    private File                apk;
    private String              apkFileName;
    private int                 productId;
    private List<Integer>       channelId;
    private String              versioncode;
    private String              versionname;

    private Map<String, Object> result           = new HashMap<String, Object>();

    @Action(value = "apkSignView", results = { @Result(name = SUCCESS, location = "/apkpack/sign.jsp") })
    public String apkSignView() {
        result.put("products", apkProductService.findAll());
        result.put("channels", apkChannelService.findAll());
        result.put("signs", apkSignService.findAll());
        result.put("keys", apkKeyService.findAll());
        return SUCCESS;
    }

    @Action(value = "apkSignTable", results = { @Result(name = SUCCESS, location = "/apkpack/signTable.jsp") })
    public String apkSignTable() {
        result.put("products", apkProductService.findAll());
        result.put("channels", apkChannelService.findAll());
        result.put("signs", apkSignService.findAll());
        result.put("keys", apkKeyService.findAll());
        return SUCCESS;
    }

    @Action(value = "apkSignPreview", results = { @Result(name = SUCCESS, type = "redirectAction", location = "apkSignView") })
    public String apkSignPreview() {

        return SUCCESS;
    }

    @Action(value = "apkSign", results = { @Result(name = SUCCESS, type = "redirectAction", location = "apkSignView") })
    public String apkSign() {
        Map<String, Object> sessionMap = ActionContext.getContext().getSession();
        String ipAddress = IpAddressUtility.getIpAddress(ServletActionContext.getRequest());

        String suffix = apkFileName.substring(apkFileName.lastIndexOf("."));
        String prefix = apkFileName.replace(suffix, "");
        ApkProductBean product = apkProductService.findById(productId);
        ApkKeyBean apkKey = apkKeyService.findById(product.getApkKeyId());

        ApkSignBean bean = new ApkSignBean();
        bean.setApkKeyId(product.getApkKeyId());
        bean.setName(prefix);
        bean.setVersioncode(versioncode);
        bean.setVersionname(versionname);
        bean.setProductId(productId);
        bean.setCreateDate(new Date());
        bean.setUploadUser((String) sessionMap.get("username"));
        bean.setUploadIp(ipAddress);

        String zipFileName = UUIDUtility.uuid32() + ".zip";
        String zipFolder = "apk";
        String signApkUrl = ImageStorageTootl.getUrl(zipFileName, zipFolder);
        bean.setSignApkFile(signApkUrl);
        int id = apkSignService.save(bean);
        try {
            ApkSignWorker.JobInfo job = new ApkSignWorker.JobInfo();
            String apkName = UUIDUtility.uuid32();
            File tempDir = FileUtility.createTempDir(apkName);
            File tempFile = File.createTempFile(apkName, suffix, tempDir);
            FileUtility.copyFile(apk, tempFile);
            job.id = id;
            job.zipFolder = zipFolder;
            job.zipFileName = zipFileName;
            job.tempDir = tempDir;
            job.tempFile = tempFile;
            job.apkName = apkName;
            job.product = product;
            job.apkKey = apkKey;
            job.suffix = suffix;
            job.versioncode = versioncode;
            job.versionname = versionname;
            job.handlerCallback = apkSignService;

            for (int channel : channelId) {
                job.channels.add(apkChannelService.findById(channel).getChannel());
            }
            ApkSignWorker.addJob(job);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String getApkFileName() {
        return apkFileName;
    }

    public void setApkFileName(String apkFileName) {
        this.apkFileName = apkFileName;
    }

    public File getApk() {
        return apk;
    }

    public void setApk(File apk) {
        this.apk = apk;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public List<Integer> getChannelId() {
        return channelId;
    }

    public void setChannelId(List<Integer> channelId) {
        this.channelId = channelId;
    }

    public String getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(String versioncode) {
        this.versioncode = versioncode;
    }

    public String getVersionname() {
        return versionname;
    }

    public void setVersionname(String versionname) {
        this.versionname = versionname;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

}
