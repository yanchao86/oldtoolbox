package com.pixshow.toolboxmgr.action;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.service.PropertiesService;

@Controller
@Scope("prototype")
@Namespace("/service")
public class PropertiesConfigAction extends BaseAction {
    private static final long serialVersionUID = 1L;

    @Autowired
    private PropertiesService propertiesService;
    /////////////////////////////////////////////////////////////
    private String            code;
    /////////////////////////////////////////////////////////////
    private JSONObject        result           = new JSONObject();

    @Action(value = "proConfig", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String proConfig() throws Exception {
        String[] codes = StringUtility.split(code, ",");
        if (codes != null && codes.length > 0) {
            List<String> values = propertiesService.getValue(codes);
            if (values != null && values.size() > 0) {
                for (int i = 0; i < codes.length; i++) {
                    result.put(codes[i], values.get(i));
                }
            }
        }
        return SUCCESS;
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
