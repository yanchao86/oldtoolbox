package com.pixshow.apkpack.action;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.apkpack.bean.ApkKeyBean;
import com.pixshow.apkpack.service.ApkKeyService;
import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.framework.utils.UUIDUtility;
import com.pixshow.toolboxmgr.tools.ImageStorageTootl;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class ApkKeyAction extends BaseAction {
    private static final long   serialVersionUID = 1L;
    @Autowired
    private ApkKeyService       apkKeyService;

    private String              name;
    private File                publicKey;
    private String              publicKeyFileName;
    private File                privateKey;
    private String              privateKeyFileName;

    private Map<String, Object> result           = new HashMap<String, Object>();

    @Action(value = "apkKey", results = { @Result(name = SUCCESS, location = "/apkpack/apkkey.jsp") })
    public String apkKey() {
        if (StringUtility.isNotEmpty(name) && publicKey.isFile()) {
            String publicKeyUrl = ImageStorageTootl.upload(UUIDUtility.uuid32() + publicKeyFileName.substring(publicKeyFileName.lastIndexOf(".")), "/apk/key", publicKey);
            String privateKeyUrl = ImageStorageTootl.upload(UUIDUtility.uuid32() + privateKeyFileName.substring(privateKeyFileName.lastIndexOf(".")), "/apk/key", privateKey);
            ApkKeyBean bean = new ApkKeyBean();
            bean.setName(name);
            bean.setPrivateKeyFile(privateKeyUrl);
            bean.setPublicKeyFile(publicKeyUrl);
            bean.setCreateDate(new Date());
            apkKeyService.save(bean);
        }

        result.put("apkKeys", apkKeyService.findAll());
        return SUCCESS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(File publicKey) {
        this.publicKey = publicKey;
    }

    public String getPublicKeyFileName() {
        return publicKeyFileName;
    }

    public void setPublicKeyFileName(String publicKeyFileName) {
        this.publicKeyFileName = publicKeyFileName;
    }

    public File getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(File privateKey) {
        this.privateKey = privateKey;
    }

    public String getPrivateKeyFileName() {
        return privateKeyFileName;
    }

    public void setPrivateKeyFileName(String privateKeyFileName) {
        this.privateKeyFileName = privateKeyFileName;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

}
