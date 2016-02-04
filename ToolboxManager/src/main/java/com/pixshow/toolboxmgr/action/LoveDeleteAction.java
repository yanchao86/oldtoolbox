package com.pixshow.toolboxmgr.action;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.pixshow.framework.support.BaseAction;
import com.pixshow.toolboxmgr.service.LoveService;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class LoveDeleteAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private LoveService loveService;

    private Integer id;

    @Override
    @Action(value = "loveDelete", results = { @Result(name = SUCCESS, type = "redirectAction", location = "loveSearch") })
    public String execute() throws Exception {

        loveService.deleteLove(id);
        return SUCCESS;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
