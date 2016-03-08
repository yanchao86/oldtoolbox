package com.pixshow.toolboxmgr.action;

import java.io.File;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.service.PropertiesService;
import com.pixshow.toolboxmgr.tools.ImageStorageTootl;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class MessageUpdate extends BaseAction {

    private static final long   serialVersionUID = 1L;

    @Autowired
    private PropertiesService   propertiesService;
    private String              appCode;
    private String              pushCode;
    // --------------------
    private String              name;
    private File                icon;
    private String              iconFileName;
    private File                toolIcon;
    private String              toolIconFileName;
    private String              adUrl;
    private String              adCheckbox;
    private File                adicon;
    private String              adiconFileName;

    private String              dlUrl;
    private String              dlCheckbox;
    private File                dlicon;
    private String              dliconFileName;
    private String              dlDoneAlert;
    private String              dlTime;

    private String              dtUrl;
    private String              dtCheck;
    private File                dticon;
    private String              dticonFileName;
    private String              marketPackage;
    private String              packageName;
    private String              versionCode;

    // ----------------------
    private static final String config_example   = "{'name':'','icon':'','packageName':'','versionCode':'200','toolbarAdvertisement':{'url':'','icon':'','active':true},'download':{'url':'','auto':true},'detail':{'url':'','open':true}}";

    @Action(value = "messageSearch", results = { @Result(name = SUCCESS, location = "/messageUpdate.jsp") })
    public String search() {
        String configName = appCode + "_CONFIG";
        ActionContext.getContext().put("config", propertiesService.getValue(configName, config_example));
        return SUCCESS;
    }

    @Action(value = "messageUpdate", results = { @Result(name = SUCCESS, type = "redirectAction", location = "messageSearch?appCode=${appCode}") })
    public String update() {
        String configName = appCode + "_CONFIG";
        String str = propertiesService.getValue(configName, config_example);

        JSONObject json = JSONObject.fromObject(str);
        JSONObject adjson = json.getJSONObject("toolbarAdvertisement");
        JSONObject dljson = json.getJSONObject("download");
        JSONObject dtjson = json.getJSONObject("detail");
        // ---
        json.put("name", name);
        json.put("marketPackage", StringUtility.isEmpty(marketPackage) ? null : marketPackage);
        json.put("packageName", packageName);

        adjson.put("url", adUrl);
        adjson.put("active", adCheckbox == null ? false : true);

        dljson.put("url", dlUrl);
        dljson.put("auto", dlCheckbox == null ? false : true);
        dljson.put("doneAlert", dlDoneAlert == null ? false : true);
        dljson.put("dlTime", dlTime);

        dtjson.put("url", dtUrl);
        dtjson.put("open", dtCheck == null ? false : true);

        json.put("versionCode", versionCode);

        if (icon != null && icon.exists()) {
            String end = iconFileName.substring(iconFileName.lastIndexOf("."));
            iconFileName = UUID.randomUUID() + end;
            ImageStorageTootl.upload(iconFileName, icon);
            json.put("icon", iconFileName);
        }
        if (adicon != null && adicon.exists()) {
            String end = adiconFileName.substring(adiconFileName.lastIndexOf("."));
            adiconFileName = UUID.randomUUID() + end;
            ImageStorageTootl.upload(adiconFileName, adicon);
            adjson.put("icon", adiconFileName);
        }
        if (toolIcon != null && toolIcon.exists()) {
            String end = toolIconFileName.substring(toolIconFileName.lastIndexOf("."));
            toolIconFileName = UUID.randomUUID() + end;
            ImageStorageTootl.upload(toolIconFileName, toolIcon);
            json.put("toolIcon", toolIconFileName);
        }
        propertiesService.setValue(configName, json.toString());

        return SUCCESS;
    }

    public String getMarketPackage() {
        return marketPackage;
    }

    public void setMarketPackage(String marketPackage) {
        this.marketPackage = marketPackage;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public String getAdCheckbox() {
        return adCheckbox;
    }

    public void setAdCheckbox(String adCheckbox) {
        this.adCheckbox = adCheckbox;
    }

    public File getIcon() {
        return icon;
    }

    public void setIcon(File icon) {
        this.icon = icon;
    }

    public String getIconFileName() {
        return iconFileName;
    }

    public void setIconFileName(String iconFileName) {
        this.iconFileName = iconFileName;
    }

    public String getDlUrl() {
        return dlUrl;
    }

    public void setDlUrl(String dlUrl) {
        this.dlUrl = dlUrl;
    }

    public String getDlCheckbox() {
        return dlCheckbox;
    }

    public void setDlCheckbox(String dlCheckbox) {
        this.dlCheckbox = dlCheckbox;
    }

    public String getDtUrl() {
        return dtUrl;
    }

    public void setDtUrl(String dtUrl) {
        this.dtUrl = dtUrl;
    }

    public String getDtCheck() {
        return dtCheck;
    }

    public void setDtCheck(String dtCheck) {
        this.dtCheck = dtCheck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getAdicon() {
        return adicon;
    }

    public void setAdicon(File adicon) {
        this.adicon = adicon;
    }

    public String getAdiconFileName() {
        return adiconFileName;
    }

    public void setAdiconFileName(String adiconFileName) {
        this.adiconFileName = adiconFileName;
    }

    public File getDlicon() {
        return dlicon;
    }

    public void setDlicon(File dlicon) {
        this.dlicon = dlicon;
    }

    public String getDliconFileName() {
        return dliconFileName;
    }

    public void setDliconFileName(String dliconFileName) {
        this.dliconFileName = dliconFileName;
    }

    public File getDticon() {
        return dticon;
    }

    public void setDticon(File dticon) {
        this.dticon = dticon;
    }

    public String getDticonFileName() {
        return dticonFileName;
    }

    public void setDticonFileName(String dticonFileName) {
        this.dticonFileName = dticonFileName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public File getToolIcon() {
        return toolIcon;
    }

    public void setToolIcon(File toolIcon) {
        this.toolIcon = toolIcon;
    }

    public String getToolIconFileName() {
        return toolIconFileName;
    }

    public void setToolIconFileName(String toolIconFileName) {
        this.toolIconFileName = toolIconFileName;
    }

    public String getPushCode() {
        return pushCode;
    }

    public void setPushCode(String pushCode) {
        this.pushCode = pushCode;
    }

    public String getDlDoneAlert() {
        return dlDoneAlert;
    }

    public void setDlDoneAlert(String dlDoneAlert) {
        this.dlDoneAlert = dlDoneAlert;
    }

    public String getDlTime() {
        return dlTime;
    }

    public void setDlTime(String dlTime) {
        this.dlTime = dlTime;
    }

}
