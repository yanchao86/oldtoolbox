package com.pixshow.apkpack.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.apkpack.bean.ApkProductBean;
import com.pixshow.apkpack.service.ApkKeyService;
import com.pixshow.apkpack.service.ApkProductService;
import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.StringUtility;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class ApkProductAction extends BaseAction {
    private static final long   serialVersionUID = 1L;
    @Autowired
    private ApkKeyService       apkKeyService;
    @Autowired
    private ApkProductService   apkProductService;

    private String              name;
    private String              engName;
    private int                 apkKeyId;
    private String              appKey;

    private Map<String, Object> result           = new HashMap<String, Object>();

    @Action(value = "apkProduct", results = { @Result(name = SUCCESS, location = "/apkpack/apkproduct.jsp") })
    public String apkProduct() {
        if (StringUtility.isNotEmpty(name) && apkKeyId > 0) {
            ApkProductBean bean = new ApkProductBean();
            bean.setApkKeyId(apkKeyId);
            bean.setName(name);
            bean.setEngName(engName);
            bean.setAppKey(appKey);
            bean.setCreateDate(new Date());
            apkProductService.save(bean);
        }
        result.put("apkKeys", apkKeyService.findAll());
        result.put("apkProducts", apkProductService.findAll());
        return SUCCESS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getApkKeyId() {
        return apkKeyId;
    }

    public void setApkKeyId(int apkKeyId) {
        this.apkKeyId = apkKeyId;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

}
