package com.pixshow.framework.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;

public abstract class BaseAction extends ActionSupport {
    protected Log log = LogFactory.getLog(getClass());
}
