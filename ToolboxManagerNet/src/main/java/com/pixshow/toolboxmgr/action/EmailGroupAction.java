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
import com.pixshow.toolboxmgr.service.EmailGroupService;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class EmailGroupAction extends BaseAction {
    private static final long   serialVersionUID = 1L;
    @Autowired
    private EmailGroupService   emailGroupService;

    private Map<String, Object> result           = new HashMap<String, Object>();

    private String              groupName;
    private int                 groupId;

    @Action(value = "addEmailGroup", results = { @Result(name = SUCCESS, type = "redirectAction", location = "findEmailGroup") })
    public String addEmailGroup() {
        emailGroupService.addEmailGroup(groupName);
        return SUCCESS;
    }

    @Action(value = "delEmailGroup", results = { @Result(name = SUCCESS, type = "redirectAction", location = "findEmailGroup") })
    public String delEmailGroup() {
        emailGroupService.delEmailGroup(groupId);
        return SUCCESS;
    }

    @Action(value = "findEmailGroup", results = { @Result(name = SUCCESS, location = "/email/groups.jsp") })
    public String findEmailGroup() {
        result.put("emailGroups", emailGroupService.findEmailGroup());
        return SUCCESS;
    }

    //////////////////////////////////////////////////////

    private String email;
    private int    emailId;
    private String emailUser;

    @Action(value = "addEmailInGroup", results = { @Result(name = SUCCESS, type = "redirectAction", location = "findEmailByGroup?groupId={groupId}") })
    public String addEmailInGroup() {
        emailGroupService.addEmailInGroup(email, emailUser, groupId);
        return SUCCESS;
    }

    @Action(value = "delEmailInGroup", results = { @Result(name = SUCCESS, type = "redirectAction", location = "findEmailByGroup?groupId={groupId}") })
    public String delEmailInGroup() {
        emailGroupService.delEmailById(emailId);
        return SUCCESS;
    }

    @Action(value = "findEmailByGroup", results = { @Result(name = SUCCESS, location = "/email/groupEmails.jsp") })
    public String findEmailByGroup() {
        result.put("emailInGroup", emailGroupService.findEmailInGroup(groupId));
        return SUCCESS;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public int getEmailId() {
        return emailId;
    }

    public void setEmailId(int emailId) {
        this.emailId = emailId;
    }

}
