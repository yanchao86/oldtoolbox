package com.pixshow.toolboxmgr.action;

import java.util.HashMap;
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
import com.pixshow.toolboxmgr.tools.AndroidPushTool;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class UpdatePushAction extends BaseAction {

    private static final long   serialVersionUID = 1L;

    @Autowired
    private PropertiesService   propertiesService;

    //TODO 改为直接推送所有信息，但要分批传送
    @Action(value = "updatePush", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String updatePush() {
        List<Map<String, Object>> values = propertiesService.likeValue("push.baidu");
        if (values != null && values.size() > 0) {
            Map<String, String> custom_content = new HashMap<String, String>();
            custom_content.put("type", "1");
            for (Map<String, Object> map : values) {
                JSONObject value = JSONObject.fromObject(map.get("value"));
                new AndroidPushTool(value.optString("appkey"), value.optString("secritkey")).pushTagMessage("优品", "优品小应用发布新版本啦！", custom_content, "push");
            }
        }
        return SUCCESS;
    }

}
