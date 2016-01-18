package com.pixshow.toolboxmgr.action;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.bean.UserBean;
import com.pixshow.toolboxmgr.service.PropertiesService;
import com.pixshow.toolboxmgr.service.UserService;
import com.pixshow.toolboxmgr.tools.AndroidPushTool;

@Controller
@Scope("prototype")
@Namespace("/service")
public class RegiesterAction extends BaseAction {
    private static final long serialVersionUID = 1L;
    @Autowired
    private UserService       userService;
    @Autowired
    private PropertiesService propertiesService;
    ///////////////////////////////////////////////////////////////////////
    private String            latlng;                                          //经纬度
    private String            mac;                                             //设备mac
    private String            pnToken;                                         //推送的token
    private String            deviceType;                                      //设备类型
    private String            osVersion;                                       //设备系统版本
    private String            appVersion;                                      //应用版本
    private int               platformVersion;                                 //平台版本
    private String            appCode;                                         //应用Code
    private String            language         = "zh_hans";                    //应用语言

    private String            userId;                                          //android 百度推送 userId
    private String            channelId;                                       //android 百度推送  channelId

    ///////////////////////////////////////////////////////////////////////
    Map<String, Object>       result           = new HashMap<String, Object>();

    @Action(value = "register", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String register() {
        int regId = 0;
        try {
            UserBean user = userService.findByMac(mac, appCode);
            if (user == null) {
                user = new UserBean();
                user.setMac(mac);
                user.setCreateDate(System.currentTimeMillis());
            } else {
                regId = user.getId();
                user.setUpdateDate(System.currentTimeMillis());
            }
            if (StringUtility.isNotEmpty(pnToken) && pnToken.length() == 64) {
                user.setPnToken(pnToken);
            }
            user.setUserId(userId);
            user.setLang(language);
            user.setChannelId(channelId);
            user.setAppVersion(appVersion);
            user.setOsVersion(osVersion);
            user.setDeviceType(deviceType);
            user.setPlatformVersion(platformVersion);
            if (StringUtility.isNotEmpty(latlng)) {
                String[] locs = latlng.split(",");
                user.setLat(Double.parseDouble(locs[0]));
                user.setLng(Double.parseDouble(locs[1]));
            }
            if (regId > 0) {
                userService.updateUser(user, appCode);
            } else {
                regId = userService.addUser(user, appCode);
                
                /**
                 * android推送tag设置 平台版本 >= 18 的走新的推送流程
                 */
                if (StringUtility.isNotEmpty(userId) && platformVersion != 0) {
                    String value = propertiesService.getValue("push.baidu." + appCode);
                    if (StringUtility.isNotEmpty(value)) {
                        JSONObject json = JSONObject.fromObject(value);
                        if (platformVersion >= 18 && platformVersion != 20/* diaxin */) {
                            new AndroidPushTool(json.optString("appkey"), json.optString("secritkey")).setTag("push2", userId);
                            new AndroidPushTool(json.optString("appkey"), json.optString("secritkey")).setTag("area_" + (regId / 10000), userId);
                        } else {
                            new AndroidPushTool(json.optString("appkey"), json.optString("secritkey")).setTag("push", userId);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("用户注册信息异常", e);
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getPnToken() {
        return pnToken;
    }

    public void setPnToken(String pnToken) {
        this.pnToken = pnToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public int getPlatformVersion() {
        return platformVersion;
    }

    public void setPlatformVersion(int platformVersion) {
        this.platformVersion = platformVersion;
    }

}
