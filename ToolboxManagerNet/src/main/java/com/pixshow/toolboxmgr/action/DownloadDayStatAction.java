/*
 * Copyright (c) 2010-2014 www.pixshow.net All Rights Reserved
 *
 * File:DownloadDayStatAction.java Project: ToolboxManager
 * 
 * Creator:4399-lvtu-8 
 * Date:2014年2月17日 下午3:09:27
 * 
 */
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
import com.pixshow.toolboxmgr.service.ToolboxService;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since 2014年2月17日
 */
@Controller
@Scope("prototype")
@Namespace("/manager")
public class DownloadDayStatAction extends BaseAction {
    private static final long   serialVersionUID = 1L;

    @Autowired
    private ToolboxService      toolboxService;

    private int                 toolId;
    private Map<String, Object> result           = new HashMap<String, Object>();

    @Action(value = "downloadDayStats", results = { @Result(name = SUCCESS, location = "/downloadDayStat.jsp") })
    public String downloadDayStat() {
        result.put("downloadDayStats", toolboxService.dlDayStats(toolId));
        return SUCCESS;
    }

    public int getToolId() {
        return toolId;
    }

    public void setToolId(int toolId) {
        this.toolId = toolId;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

}
