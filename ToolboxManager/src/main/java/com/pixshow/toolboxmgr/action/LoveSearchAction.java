package com.pixshow.toolboxmgr.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.support.BaseAction;
import com.pixshow.toolboxmgr.bean.LoveBean;
import com.pixshow.toolboxmgr.service.LoveService;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class LoveSearchAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private LoveService loveService;

    private List<LoveBean> list;

    @Override
    @Action(value = "loveSearch", results = { @Result(name = SUCCESS, location = "/loveList.jsp") })
    public String execute() throws Exception {
        list = loveService.searchLove();
        return SUCCESS;
    }

    public List<LoveBean> getList() {
        return list;
    }

    public void setList(List<LoveBean> list) {
        this.list = list;
    }

}
