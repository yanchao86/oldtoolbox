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

import com.pixshow.framework.support.BaseAction;
import com.pixshow.toolboxmgr.bean.ToolboxPackageBean;
import com.pixshow.toolboxmgr.service.ToolboxPackageService;

@Controller
@Scope("prototype")
@Namespace("/service")
public class ToolboxPackageSearchAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ToolboxPackageService toolboxService;

    private JSONArray result = new JSONArray();

    @Override
    @Action(value = "packageSearch", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String execute() throws Exception {
        List<ToolboxPackageBean> list = toolboxService.searchPackage();
        for (ToolboxPackageBean bean : list) {
            JSONObject json = new JSONObject();
           // json.put("toolboxId", bean.getToolboxId());
            json.put("packageName", bean.getPackageName());

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
