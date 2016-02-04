/*
 * Copyright (c) 2010-2014 www.pixshow.net All Rights Reserved
 *
 * File:CustomConfigAction.java Project: ToolboxManager
 * 
 * Creator:4399-lvtu-8 
 * Date:2014年3月7日 上午4:08:48
 * 
 */
package com.pixshow.custom.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.custom.grid.CustomGridService;
import com.pixshow.framework.support.BaseAction;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$
 * @since 2014年3月7日
 */
@Controller
@Scope("prototype")
@Namespace("/manager")
public class CustomConfigAction extends BaseAction {
    private static final long   serialVersionUID = 1L;
    @Autowired
    private CustomConfigService configService;
    @Autowired
    private CustomGridService   customGridService;
    ///////////////////////////////////////////
    private String              code;
    private String              name;
    private String              definition;
    private String              value;
    ///////////////////////////////////////////
    private Map<String, Object> result           = new HashMap<String, Object>();

    @Action(value = "customCreate", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String customCreate() {
        configService.saveDef(code, name, definition);
        return SUCCESS;
    }

    @Action(value = "customUpdate", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String customUpdate() {
        configService.updateDef(code, name, definition);
        return SUCCESS;
    }

    @Action(value = "defList", results = { @Result(name = SUCCESS, location = "/custom/config/defList.jsp") })
    public String defList() {
        result.put("defList", configService.defList());
        return SUCCESS;
    }

    @Action(value = "customList", results = { @Result(name = SUCCESS, location = "/custom/config/list.jsp") })
    public String customList() {
        result.put("defList", configService.defList());
        return SUCCESS;
    }

    @Action(value = "defInfo", results = { @Result(name = SUCCESS, location = "/custom/config/defCreate.jsp") })
    public String defInfo() {
        result.put("def", configService.findDefByCode(code));
        result.put("allGrid", customGridService.allGridTable());
        return SUCCESS;
    }

    @Action(value = "customInfo", results = { @Result(name = SUCCESS, location = "/custom/config/defInfo.jsp") })
    public String customInfo() {
        result.put("def", configService.findDefByCode(code));
        result.put("defVal", configService.findValsByDefCode(code));
        result.put("allGrid", customGridService.allGridTable());
        return SUCCESS;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
