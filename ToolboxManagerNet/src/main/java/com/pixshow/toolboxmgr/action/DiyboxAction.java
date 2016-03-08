package com.pixshow.toolboxmgr.action;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.bean.DiyboxBean;
import com.pixshow.toolboxmgr.service.DiyboxService;
import com.pixshow.toolboxmgr.service.PropertiesService;
import com.pixshow.toolboxmgr.tools.ImageStorageTootl;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class DiyboxAction extends BaseAction {
    private static final long   serialVersionUID = 1L;
    //////////////////////////////////////////////////////////
    @Autowired
    private DiyboxService       diyboxService;
    @Autowired
    private PropertiesService   propertiesService;
    //////////////////////////////////////////////////////////
    private Integer             id;
    private String              name;
    private File                icon;
    private String              iconFileName;
    private String              downloadUrl;
    private int                 downloadAuto;
    private String              detailUrl;
    private int                 detailOpen;
    private float               rate;
    private String              packageName;
    private String              versionCode;
    // 排序
    private String              strID;

    //////////////////////////////////////////////////////////
    private Map<String, Object> result           = new HashMap<String, Object>();

    @Action(value = "diyInsert", results = { @Result(name = SUCCESS, type = "redirectAction", location = "diySearch") })
    public String diyInsert() {
        String endName = iconFileName.substring(iconFileName.lastIndexOf("."));
        iconFileName = UUID.randomUUID() + endName;
        ImageStorageTootl.upload(iconFileName, icon);

        DiyboxBean bean = new DiyboxBean();
        bean.setName(name);
        bean.setSortIndex(0);
        bean.setIcon(iconFileName);
        bean.setDownloadUrl(downloadUrl);
        bean.setDownloadAuto(downloadAuto);
        bean.setDownloadCount(0);
        bean.setDetailUrl(detailUrl);
        bean.setDetailOpen(detailOpen);
        bean.setRate(rate);
        bean.setPackageName(packageName);
        bean.setVersionCode(versionCode);
        bean.setCreateDate(new Date());
        bean.setUpdateDate(new Date());
        diyboxService.insertTool(bean);
        return SUCCESS;
    }

    @Action(value = "diySearch", results = { @Result(name = SUCCESS, location = "/diybox/diyboxList.jsp") })
    public String diySearch() {
        result.put("diyboxs", diyboxService.searchTool());
        return SUCCESS;
    }

    @Action(value = "diyPreUpdate", results = { @Result(name = SUCCESS, location = "/diybox/diyboxUpdate.jsp") })
    public String diyPreUpdate() {
        result.put("diybox", diyboxService.searchByIDTool(id));
        return SUCCESS;
    }

    @Action(value = "diyUpdate", results = { @Result(name = SUCCESS, type = "redirectAction", location = "diySearch") })
    public String diyUpdate() {
        DiyboxBean bean = diyboxService.searchByIDTool(id);
        bean.setName(name);
        bean.setRate(rate);
        bean.setDownloadUrl(downloadUrl);
        bean.setDownloadAuto(downloadAuto);
        bean.setDetailUrl(detailUrl);
        bean.setDetailOpen(detailOpen);
        bean.setPackageName(packageName);
        bean.setVersionCode(versionCode);
        bean.setUpdateDate(new Date());
        if (icon != null) {
            if (StringUtility.isEmpty(bean.getIcon())) {
                String end = iconFileName.substring(iconFileName.lastIndexOf("."));
                iconFileName = UUID.randomUUID() + end;
                bean.setIcon(iconFileName);
            }
            ImageStorageTootl.upload(bean.getIcon(), icon);
        }
        diyboxService.updateTool(bean);
        return SUCCESS;
    }

    @Action(value = "diyDelete", results = { @Result(name = SUCCESS, type = "redirectAction", location = "diySearch") })
    public String diyDelete() {
        diyboxService.deleteTool(id);
        return SUCCESS;
    }

    @Action(value = "diySort", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String diySort() {
        String[] strs = strID.split(",");
        for (int i = 0; i < strs.length; i++) {
            int id = Integer.parseInt(strs[i]);
            diyboxService.sortTool(id, i);
        }
        return SUCCESS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String get() {
        return strID;
    }

    public void setStrID(String strID) {
        this.strID = strID;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getDownloadAuto() {
        return downloadAuto;
    }

    public void setDownloadAuto(int downloadAuto) {
        this.downloadAuto = downloadAuto;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public int getDetailOpen() {
        return detailOpen;
    }

    public void setDetailOpen(int detailOpen) {
        this.detailOpen = detailOpen;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public String getStrID() {
        return strID;
    }

}
