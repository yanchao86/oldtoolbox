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

import com.pixshow.framework.support.BaseAction;
import com.pixshow.toolboxmgr.service.PropertiesService;
import com.pixshow.toolboxmgr.tools.ImageStorageTootl;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class ToolboxSettingAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private PropertiesService propertiesService;

    private File              icon;
    private String            iconFileName;
    private String            chTitle;
    private String            enTitle;
    private String            subChTitle;
    private String            subEnTitle;
    //////////////////////////////////////////////////////////////
    private JSONObject        result           = new JSONObject();

    @Action(value = "toolboxInfo", results = { @Result(name = SUCCESS, location = "/toolbox/toolboxInfo.jsp") })
    public String toolboxInfo() {
        result = JSONObject.fromObject(propertiesService.getValue("TOOLBOX_INFO"));
        return SUCCESS;
    }

    @Action(value = "toolboxInfoUpdate", results = { @Result(name = SUCCESS, type = "redirectAction", location = "toolboxInfo") })
    public String toolboxInfoUpdate() {
        JSONObject data = new JSONObject();
        data.put("chTitle", chTitle);
        data.put("enTitle", enTitle);
        data.put("subChTitle", subChTitle);
        data.put("subEnTitle", subEnTitle);
        if (icon != null) {
            String endName = iconFileName.substring(iconFileName.lastIndexOf("."));
            iconFileName = UUID.randomUUID() + endName;
            ImageStorageTootl.upload(iconFileName, icon);
            data.put("icon", ImageStorageTootl.getUrl(iconFileName));
        }
        propertiesService.updateValue("TOOLBOX_INFO", data.toString());
        return SUCCESS;
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

    public String getChTitle() {
        return chTitle;
    }

    public void setChTitle(String chTitle) {
        this.chTitle = chTitle;
    }

    public String getEnTitle() {
        return enTitle;
    }

    public void setEnTitle(String enTitle) {
        this.enTitle = enTitle;
    }

    public String getSubChTitle() {
        return subChTitle;
    }

    public void setSubChTitle(String subChTitle) {
        this.subChTitle = subChTitle;
    }

    public String getSubEnTitle() {
        return subEnTitle;
    }

    public void setSubEnTitle(String subEnTitle) {
        this.subEnTitle = subEnTitle;
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

}
