package com.pixshow.toolboxmgr.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.support.BaseAction;
import com.pixshow.toolboxmgr.tools.AliyunTool;

import net.sf.json.JSONObject;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Controller
@Scope("prototype")
@Namespace("/manager")
public class AliYunCDNAction extends BaseAction {
    private static final long serialVersionUID = 1L;
    
    private JSONObject result = new JSONObject();
    private String     type;
    private String     path;

    @Action(value = "refreshCDN", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String refreshCDN() {
        String res = AliyunTool.refreshCDN(type, path);
        result.put("msg", res);
        return SUCCESS;
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
