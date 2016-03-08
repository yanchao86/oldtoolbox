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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.StringUtility;
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
@Namespace("/manager")
public class ChannelToolSourceAction extends BaseAction {
    private static final long   serialVersionUID = 1L;
    ////////////////////////////////////////////////////////
    @Autowired
    private PropertiesService   propertiesService;
    ////////////////////////////////////////////////////////
    private String              prefix           = "channel_";
    private String              key;
    private String              value;
    ////////////////////////////////////////////////////////
    private Map<String, Object> result           = new HashMap<String, Object>();

    @Action(value = "channelToolSource", results = { @Result(name = SUCCESS, location = "/channelToolSource.jsp") })
    public String channelToolSource() {
        List<Map<String, Object>> values = propertiesService.getValues(prefix);
        result.put("values", values);
        return SUCCESS;
    }

    @Action(value = "saveChannelToolSource", results = { @Result(name = SUCCESS, type = "redirectAction", location = "channelToolSource") })
    public String saveChannelToolSource() {
        if (StringUtility.isEmpty(propertiesService.getValue(key))) {
            propertiesService.setValue(prefix + key, value);
        }
        return SUCCESS;
    }

    @Action(value = "channelChange", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String channelChange() {
        propertiesService.updateValue(key, value);
        return SUCCESS;
    }
    @Action(value = "delchannel", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String delchannel() {
        propertiesService.delchannel(key);
        return SUCCESS;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
