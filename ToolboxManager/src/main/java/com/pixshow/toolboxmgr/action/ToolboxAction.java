package com.pixshow.toolboxmgr.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.support.BaseAction;
import com.pixshow.toolboxmgr.bean.ToolboxBean;
import com.pixshow.toolboxmgr.service.PropertiesService;
import com.pixshow.toolboxmgr.service.ToolboxService;
import com.pixshow.toolboxmgr.tools.CheckUrlUtil;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class ToolboxAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ToolboxService toolboxService;

    private Integer           id;
    private String            strID;
    // ------
    private List<ToolboxBean> list = new ArrayList<ToolboxBean>();

    private ToolboxBean toolboxBean;

    @Action(value = "selfswitch", results = { @Result(name = SUCCESS, location = "/toolbox/selfswitch.jsp") })
    public String selfswitch() {

        return SUCCESS;
    }

    @Action(value = "toolSearch", results = { @Result(name = SUCCESS, location = "/toolbox/toolboxList.jsp") })
    public String toolSearch() {
        list = toolboxService.searchTool();
        return SUCCESS;
    }

    @Action(value = "toolPreUpdate", results = { @Result(name = SUCCESS, location = "/toolbox/toolboxUpdate.jsp") })
    public String toolPreUpdate() {
        toolboxBean = toolboxService.searchByIDTool(id);
        return SUCCESS;
    }

    @Action(value = "toolDelete", results = { @Result(name = SUCCESS, type = "redirectAction", location = "toolSearch") })
    public String toolDelete() {
        toolboxService.deleteTool(id);
        return SUCCESS;
    }

    @Action(value = "toolSort", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String toolSort() {
        String[] strs = strID.split(",");
        for (int i = 0; i < strs.length; i++) {
            int id = Integer.parseInt(strs[i]);
            toolboxService.sortTool(id, i);
        }
        return SUCCESS;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStrID() {
        return strID;
    }

    public void setStrID(String strID) {
        this.strID = strID;
    }

    public List<ToolboxBean> getList() {
        return list;
    }

    public void setList(List<ToolboxBean> list) {
        this.list = list;
    }

    public ToolboxBean getToolboxBean() {
        return toolboxBean;
    }

    public void setToolboxBean(ToolboxBean toolboxBean) {
        this.toolboxBean = toolboxBean;
    }

}
