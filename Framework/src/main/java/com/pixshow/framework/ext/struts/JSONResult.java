/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:JSONResult.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jan 18, 2013 2:15:48 PM
 * 
 */
package com.pixshow.framework.ext.struts;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.StrutsStatics;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;
import com.pixshow.framework.abbr.api.annotation.Abbr;
import com.pixshow.framework.config.Config;
import com.pixshow.framework.utils.AnnotationUtility;
import com.pixshow.framework.utils.ClassUtility;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Jan 18, 2013
 * 
 */

public class JSONResult extends org.apache.struts2.json.JSONResult {

    private static final long serialVersionUID = 1L;

    protected Object findRootObject(ActionInvocation invocation) {
        Object rootObject;
        if (getRoot() != null) {
            ValueStack stack = invocation.getStack();
            rootObject = stack.findValue(getRoot());
        } else {
            rootObject = invocation.getStack().peek(); // model overrides action
        }
        return rootObject;
    }

    protected Object readRootObject(ActionInvocation invocation) {
        if (isEnableSMD()) {
            return buildSMDObject(invocation);
        }
        return findRootObject(invocation);
    }

    protected boolean enableGzip(HttpServletRequest request) {
        return isEnableGZIP() && JSONUtil.isGzipInRequest(request);
    }

    @Override
    public void execute(ActionInvocation invocation) throws Exception {
        ActionContext actionContext = invocation.getInvocationContext();
        HttpServletRequest request = (HttpServletRequest) actionContext.get(StrutsStatics.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) actionContext.get(StrutsStatics.HTTP_RESPONSE);

        boolean enableAbbr = false;
        boolean excludeEmpty = false;
        Class<?> actionClass = ClassUtility.getUserClass(invocation.getAction().getClass());
        Abbr abbr = AnnotationUtility.findAnnotation(actionClass, Abbr.class);
        if (abbr == null) {
            enableAbbr = Config.getInstance().getBoolean("abbr.setting.enable.defalut", enableAbbr);
            excludeEmpty = Config.getInstance().getBoolean("abbr.setting.excludeEmpty.defalut", enableAbbr);
        } else {
            enableAbbr = abbr.enable();
            excludeEmpty = abbr.excludeEmpty();
        }

        try {
            Object rootObject;
            rootObject = readRootObject(invocation); 
            writeToResponse(response, createJSONString(request, rootObject, enableAbbr, excludeEmpty), enableGzip(request));
        } catch (IOException exception) {
            throw exception;
        }
    }

    private String createJSONString(HttpServletRequest request, Object rootObject, boolean enableAbbr, boolean excludeEmpty) throws JSONException {
        String json;
        JSONWriter writer = new JSONWriter();
        writer.setIgnoreHierarchy(isIgnoreHierarchy());
        writer.setEnumAsBean(isEnumAsBean());
        writer.setEnableAbbr(enableAbbr);
        writer.setExcludeEmpty(excludeEmpty);
        json = writer.write(rootObject, getExcludePropertiesList(), getIncludePropertiesList(), isExcludeNullProperties());

        // json = JSONUtil.serialize(rootObject, getExcludePropertiesList(), getIncludePropertiesList(), isIgnoreHierarchy(), isEnumAsBean(), isExcludeNullProperties());
        json = "{\"result\":0,\"data\":" + json + "}";
        json = addCallbackIfApplicable(request, json);
        return json;
    }
}
