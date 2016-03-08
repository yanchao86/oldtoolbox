package com.pixshow.toolboxmgr.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.support.BaseAction;
import com.pixshow.toolboxmgr.service.PropertiesService;
import com.pixshow.toolboxmgr.tools.LargeDataPush;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class PushDataAction extends BaseAction {
    private static final long serialVersionUID = 1L;
    @Autowired
    private PropertiesService propertiesService;

    private List<String>      url              = new ArrayList<String>();

    private JSONObject        result           = new JSONObject();

    @Action(value = "pushData", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String pushData() {
        List<Map<String, Object>> values = propertiesService.likeValue("push.baidu");
        if (values == null || values.size() == 0) {
            result.put("error", "no push source");
            return SUCCESS;
        }
        for (Map<String, Object> map : values) {
            JSONObject value = JSONObject.fromObject(map.get("value"));
            LargeDataPush.pushData(url, value.optString("appkey"), value.optString("secritkey"), "push2");
        }
        return SUCCESS;
    }

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

}
