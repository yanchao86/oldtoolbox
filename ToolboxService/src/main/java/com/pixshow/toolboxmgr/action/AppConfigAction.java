package com.pixshow.toolboxmgr.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.redis.RedisToolboxService;
import com.pixshow.toolboxmgr.service.PropertiesService;
import com.pixshow.toolboxmgr.tools.ImageStorageTootl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@Scope("prototype")
@Namespace("/service")
public class AppConfigAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private PropertiesService   propertiesService;
    @Autowired
    private RedisToolboxService redisToolboxService;

    private Object                result = null;
    private static final String[] keys   = new String[] { "APP1_CONFIG", "APP2_CONFIG", "APP3_CONFIG", "APP4_CONFIG", "APP5_CONFIG" };

    @Action(value = "appConfig", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String appConfig() {
        this.result = new JSONObject();
        boolean fromredis = false;
        String rKey = "appConfig@APP_CONFIG";
        if (redisToolboxService.check(rKey)) {
            try {
                result = JSONObject.fromObject(redisToolboxService.get(rKey));
                fromredis = true;
            } catch (Exception e) {
                log.info(rKey + " to jsonarr error");
            }
        }
        if (fromredis) {
            return SUCCESS;
        }

        List<String> list = propertiesService.getValue(keys);
        HttpServletRequest request = ServletActionContext.getRequest();
        String query = request.getQueryString();

        for (int i = 0; i < keys.length; i++) {
            String code = keys[i];
            code = code.substring(0, code.indexOf("_"));
            if (list.get(i) == null) {
                continue;
            }
            JSONObject json = JSONObject.fromObject(list.get(i));
            if (StringUtility.isNotEmpty(query) && query.contains("FLASHLIGHT")) {
                json.put("icon", "http://test.oss.aliyuncs.com/error.png");
            } else {
                json.put("icon", ImageStorageTootl.getUrl(json.getString("icon")));
            }
            json.put("toolIcon", ImageStorageTootl.getUrl(json.optString("toolIcon")));
            JSONObject adjson = json.getJSONObject("toolbarAdvertisement");
            adjson.put("icon", ImageStorageTootl.getUrl(adjson.getString("icon")));
            ((JSONObject) result).put(code, json);
        }
        try {
            redisToolboxService.set(rKey, ((JSONObject) result).toString());
        } catch (Exception e) {
            log.info(rKey + " to set redis error");
        }
        return SUCCESS;
    }

    @Action(value = "appConfigArray", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String appConfigArray() {
        this.result = new JSONArray();
        boolean fromredis = false;
        String rKey = "appConfig@APP_CONFIG";
        if (redisToolboxService.check(rKey)) {
            try {
                result = JSONArray.fromObject(redisToolboxService.get(rKey));
                fromredis = true;
            } catch (Exception e) {
                log.info(rKey + " to jsonarr error");
            }
        }
        if (fromredis) {
            return SUCCESS;
        }

        List<String> list = propertiesService.getValue(keys);
        HttpServletRequest request = ServletActionContext.getRequest();
        String query = request.getQueryString();
        for (int i = 0; i < keys.length; i++) {
            String code = keys[i];
            code = code.substring(0, code.indexOf("_"));
            if (list.get(i) == null) {
                continue;
            }
            JSONObject json = JSONObject.fromObject(list.get(i));
            json.put("code", code);
            if (StringUtility.isNotEmpty(query) && query.contains("FLASHLIGHT")) {
                json.put("icon", "http://test.oss.aliyuncs.com/error.png");
            } else {
                json.put("icon", ImageStorageTootl.getUrl(json.getString("icon")));
            }
            json.put("toolIcon", ImageStorageTootl.getUrl(json.optString("toolIcon")));
            JSONObject adjson = json.getJSONObject("toolbarAdvertisement");
            adjson.put("icon", ImageStorageTootl.getUrl(adjson.getString("icon")));
            ((JSONArray) result).add(json);
        }

        try {
            redisToolboxService.set(rKey, ((JSONArray) result).toString());
        } catch (Exception e) {
            log.info(rKey + " to set redis error");
        }
        return SUCCESS;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

}
