package com.pixshow.toolboxmgr.action;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.config.Config;
import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.DateUtility;
import com.pixshow.toolboxmgr.bean.DiyboxBean;
import com.pixshow.toolboxmgr.service.DiyboxService;
import com.pixshow.toolboxmgr.tools.ImageStorageTootl;

@Controller
@Scope("prototype")
@Namespace("/service")
public class DiyboxSearchAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private DiyboxService     diyboxService;

    private JSONArray         result           = new JSONArray();

    @Action(value = "diySearch", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String execute() throws Exception {
        List<DiyboxBean> list = diyboxService.searchDiys();
        for (DiyboxBean bean : list) {
            JSONObject json = new JSONObject();
            json.put("id", bean.getId());
            json.put("name", bean.getName());
            json.put("sortIndex", bean.getSortIndex());
            json.put("icon", ImageStorageTootl.getUrl(bean.getIcon()));
            json.put("downloadUrl", Config.getInstance().getString("toolbox.download.baseUrl") + "service/download.do?diyId=" + bean.getId());
            json.put("downloadAuto", bean.getDownloadAuto() == 0 ? false : true);
            json.put("downloadCount", bean.getDownloadCount());
            json.put("detailUrl", bean.getDetailUrl());
            json.put("detailOpen", bean.getDetailOpen() == 0 ? false : true);
            json.put("rate", bean.getRate());
            json.put("packageName", bean.getPackageName());
            json.put("versionCode", bean.getVersionCode());
            json.put("createDate", DateUtility.format(bean.getCreateDate()));
            json.put("updateDate", DateUtility.format(bean.getUpdateDate()));

            result.add(json);
        }
        return SUCCESS;
    }

    public JSONArray getResult() {
        return result;
    }

    public void setResult(JSONArray result) {
        this.result = result;
    }
}
