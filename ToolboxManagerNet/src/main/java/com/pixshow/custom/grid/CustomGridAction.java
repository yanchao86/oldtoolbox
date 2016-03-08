package com.pixshow.custom.grid;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.support.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class CustomGridAction extends BaseAction {
    private static final long   serialVersionUID = 1L;
    @Autowired
    private CustomGridService   customGridService;

    private String              code;
    private String              name;
    private String              definition;

    ///////////////////////////////////////////
    private Map<String, Object> result           = new HashMap<String, Object>();

    @Action(value = "gridList", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String gridList() {
        result.put("gridList", customGridService.gridCodeList());
        return SUCCESS;
    }

    @Action(value = "gridInfo", results = { @Result(name = SUCCESS, location = "/custom/grid/addGrid.jsp") })
    public String gridInfo() {
        result.put("gridInfo", customGridService.findGridByCode(code));
        return SUCCESS;
    }

    @Action(value = "addGrid", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String addGrid() {
        customGridService.saveGrid(code, name, definition);
        return SUCCESS;
    }
    
    @Action(value = "updateGrid", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String updateGrid() {
        customGridService.updateGrid(code, name, definition);
        return SUCCESS;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

}
