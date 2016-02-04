package com.pixshow.apkpack.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.apkpack.bean.ApkChannelBean;
import com.pixshow.apkpack.service.ApkChannelService;
import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.StringUtility;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class ApkChannekAction extends BaseAction {
    private static final long   serialVersionUID = 1L;
    @Autowired
    private ApkChannelService   apkChannelService;

    private String              name;
    private String              channel;

    private Map<String, Object> result           = new HashMap<String, Object>();

    @Action(value = "apkChannel", results = { @Result(name = SUCCESS, location = "/apkpack/apkchannel.jsp") })
    public String apkChannel() {
        if (StringUtility.isNotEmpty(name) && StringUtility.isNotEmpty(channel) && apkChannelService.findByChannel(channel) == null) {
            ApkChannelBean bean = new ApkChannelBean();
            bean.setName(name);
            bean.setChannel(channel);
            bean.setCreateDate(new Date());
            apkChannelService.save(bean);
        }
        result.put("apkChannels", apkChannelService.findAll());
        return SUCCESS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

}
