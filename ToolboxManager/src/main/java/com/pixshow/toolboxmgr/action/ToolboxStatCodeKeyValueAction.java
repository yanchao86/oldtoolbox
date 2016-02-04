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
public class ToolboxStatCodeKeyValueAction extends BaseAction {
    private static final long   serialVersionUID = 1L;
    @Autowired
    private StatCodeService     statCodeService;
    ///////////////////////////////////////////////
    private int                 id;
    private String              name;
    private String              value;
    ///////////////////////////////////////////////
    private Map<String, Object> result           = new HashMap<String, Object>();

    @Action(value = "statCodeKeyValues", results = { @Result(name = SUCCESS, location = "/stat/keyValue.jsp") })
    public String statCodeKeyValues() {
        result.put("keyValues", statCodeService.findKeyValueOrderById());
        return SUCCESS;
    }

    @Action(value = "saveKeyValue", results = { @Result(name = SUCCESS, type = "redirectAction", location = "statCodeKeyValues") })
    public String addStatCat() {
        statCodeService.insertKeyValue(name, value);
        return SUCCESS;
    }

    @Action(value = "updateKeyValue", results = { @Result(name = SUCCESS, type = "redirectAction", location = "statCodeKeyValues") })
    public String updateKeyValue() {
        statCodeService.updateKeyValue(id, value);
        return SUCCESS;
    }

    @Action(value = "deleteKeyValue", results = { @Result(name = SUCCESS, type = "redirectAction", location = "statCodeKeyValues") })
    public String deleteKeyValue() {
        statCodeService.deleteKeyValue(id);
        return SUCCESS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
