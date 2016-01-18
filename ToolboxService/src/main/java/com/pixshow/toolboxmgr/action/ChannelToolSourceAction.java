/*
 * Copyright (c) 2010-2014 www.pixshow.net All Rights Reserved
 *
 * File:ChannelToolSourceAction.java Project: ToolboxManager
 * 
 * Creator:4399-lvtu-8 
 * Date:2014年3月6日 上午8:25:42
 * 
 */
package com.pixshow.toolboxmgr.action;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.support.BaseAction;
import com.pixshow.toolboxmgr.service.DownloadService;
import com.pixshow.toolboxmgr.service.PropertiesService;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$
 * @since 2014年3月6日
 */
@Controller
@Scope("prototype")
@Namespace("/service")
public class ChannelToolSourceAction extends BaseAction {
    private static final long serialVersionUID = 1L;
    ////////////////////////////////////////////////////////
    @Autowired
    private PropertiesService propertiesService;
    @Autowired
    private DownloadService   downloadService;
    ////////////////////////////////////////////////////////
    private String            prefix           = "channel_";
    private String            code;
    ////////////////////////////////////////////////////////
    private JSONObject        result           = new JSONObject();

    @Action(value = "channelToolSource", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String channelToolSource() {
        result.put("value", propertiesService.getValue(prefix + code));
        return SUCCESS;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

}
