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

import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.CipherUtility;
import com.pixshow.framework.utils.DateUtility;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.service.PropertiesService;
import com.pixshow.toolboxmgr.service.StatService;

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
@Namespace("/service")
public class ToolboxStatAction extends BaseAction {
    private static final long serialVersionUID = 1L;
    ////////////////////////////////////////////////////////
    @Autowired
    private StatService       statService;
    ////////////////////////////////////////////////////////
    @Deprecated
    private String            mac;
    @Deprecated
    private String            code;
    @Deprecated
    private String            type             = "pv";            //uv pv

    ////////////////////////////////////////////////////////
    private JSONObject        result           = new JSONObject();

    @Deprecated
    @Action(value = "countStatistics", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String countStatistics() {
        if ("uv".equals(type)) {
            statService.uvStat(mac, code, new Date());
        } else {
            statService.pvStat(code, 1, new Date());
        }
        return SUCCESS;
    }

    private String flag;

    @Action(value = "bmw", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String bmw() {
//        try {
//            JSONObject json = JSONObject.fromObject(CipherUtility.AES.decrypt(flag, "dsjkfh824hnlkdfnmvo"));
//            String productCode = json.optString("productCode");
//            String productVersion = json.optString("productVersion");
//            String sdkVersion = json.optString("toolSdkVersion");
//
//            String macVal = json.optString("mac");
//            String codeVal = json.optString("code");
//            String typeVal = json.optString("type");
//            if ("uv".equals(typeVal)) {
//                if (StringUtility.isNotEmpty(productCode)) {
//                    statService.uvStat(codeVal, productCode, productVersion, sdkVersion, macVal, new Date());
//                }
//                statService.uvStat(macVal, codeVal, new Date());
//            } else {
//                if (StringUtility.isNotEmpty(productCode)) {
//                    statService.pvStat(codeVal, productCode, productVersion, sdkVersion, 1, new Date());
//                }
//                statService.pvStat(codeVal, 1, new Date());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return SUCCESS;
    }

    @Action(value = "bkw", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String bkw() {
//        statService.bkw(CipherUtility.AES.decrypt(flag, "dsjkfh824hnlkdfnmvo"));
        return SUCCESS;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public static void main(String[] args) {
        String str = "5VBgvi0JtUzAOdATJQmZOrbE_73Yj8AUDpUZ2rE2YZYqDeDTcES5IUEP0eHZ4nFogqY7HdNqo4ycWXrPidvB_AMkZTziAuiZ6_DsTIVcSXQ4AwPLs8adwJpYbug64kjDlgiDkVKr-B_t-DgPQ4m_1DqQwPCvpgw5ESLO3b2Zah0";
        System.out.println(CipherUtility.AES.decrypt(str, "dsjkfh824hnlkdfnmvo"));
        JSONObject json = new JSONObject();
        json.put("mac", "b8:b4:2e:74:c9:e2");
        JSONArray queue = new JSONArray();
        JSONObject item1 = new JSONObject();
        item1.put("type", "pv");
        item1.put("code", "dx_20140418|com.zhiqupk.ziti_toolbox_from_notifcation_listitem_download");
        item1.put("count", 2);
        queue.add(item1);
        JSONObject item2 = new JSONObject();
        item2.put("type", "uv");
        item2.put("code", "dx_20140418|notifcation_version_num_20");
        item2.put("unixtime", DateUtility.currentUnixTime());
        queue.add(item2);
        json.put("queue", queue);
        System.out.println(CipherUtility.AES.encrypt(json.toString(), "dsjkfh824hnlkdfnmvo"));
    }

}
