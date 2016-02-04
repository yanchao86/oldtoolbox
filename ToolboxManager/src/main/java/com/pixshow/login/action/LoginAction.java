/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:CityListAction.java Project: LvPhotoScenery
 * 
 * Creator:4399-lvtu-8 
 * Date:Jan 30, 2013 7:06:42 PM
 * 
 */
package com.pixshow.login.action;

import java.io.File;
import java.io.IOException;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.FileUtility;
import com.pixshow.framework.utils.StringUtility;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$
 * @since Jan 30, 2013
 * 
 */

@Controller
@Scope("prototype")
@Namespace("/")
public class LoginAction extends BaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String            url;
    private String            username;
    private String            password;
    private String            target;

    private String            message;

    @Action(value = "login", results = {//
    @Result(name = SUCCESS, type = "redirect", location = "/index.jsp"),// 默认登录成功地址
            @Result(name = "successRedirectUrl", type = "redirect", location = "${url}"), //登录成功跳转地址
            @Result(name = ERROR, location = "/login.jsp") // 登录失败
    })
    public String login() {
        String data = null;
        try {
            data = FileUtility.readFileToString(new File(this.getClass().getResource("/config/config_admin.json").getFile()), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject json = JSONObject.fromObject(data);
        JSONObject user = json.getJSONObject("user");

        if (!user.containsKey(username) || !password.equals(user.getJSONObject(username).optString("password"))) {
            message = "密码错误";
            return ERROR;
        }
        ActionContext actionContext = ActionContext.getContext();
        actionContext.getSession().put("login", "ok");
        actionContext.getSession().put("username", username);
        if (StringUtility.isNotEmpty(url)) { return "successRedirectUrl"; }
        return SUCCESS;
    }

    @Action(value = "logout", results = { @Result(name = SUCCESS, type = "redirect", location = "/index.jsp") })
    public String logout() {
        ServletActionContext.getRequest().getSession().invalidate();
        return SUCCESS;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
