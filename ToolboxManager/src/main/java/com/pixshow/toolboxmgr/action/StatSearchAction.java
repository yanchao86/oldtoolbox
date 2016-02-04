package com.pixshow.toolboxmgr.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.support.BaseAction;
import com.pixshow.toolboxmgr.service.StatCodeService;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class StatSearchAction extends BaseAction {
    private static final long   serialVersionUID = 1L;
    @Autowired
    private StatCodeService     statCodeService;

    private String              keyCode;

    private Map<String, Object> result           = new HashMap<String, Object>();

    @Action(value = "statByCode", results = { @Result(name = SUCCESS, location = "/stat/statSearch.jsp") })
    public String statByCode() {
        result.put("stats", statCodeService.statByLikeCode(keyCode));
        return SUCCESS;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

}
