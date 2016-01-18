package com.pixshow.toolboxmgr.action;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.config.Config;
import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.DateUtility;
import com.pixshow.redis.RedisToolboxService;
import com.pixshow.toolboxmgr.bean.ToolboxBean;
import com.pixshow.toolboxmgr.service.ToolboxService;
import com.pixshow.toolboxmgr.tools.ImageStorageTootl;

@Controller
@Scope("prototype")
@Namespace("/service")
public class ToolboxSearchAction extends BaseAction {
    private static final long   serialVersionUID = 1L;
    private final static Log    log              = LogFactory.getLog(ToolboxSearchAction.class);

    @Autowired
    private ToolboxService      toolboxService;
    @Autowired
    private RedisToolboxService redisToolboxService;

    private int                 index            = -1;
    private int                 items            = 9999;

    private JSONArray           result           = new JSONArray();

    @Action(value = "toolSearch", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String execute() throws Exception {
        boolean fromredis = false;
        String rKey = "toolSearch@" + index + "_" + items;
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
        //#########################################################//

        List<ToolboxBean> list = toolboxService.searchTool(index, items);
        for (ToolboxBean bean : list) {
            JSONObject json = new JSONObject();
            json.put("id", bean.getId());
            json.put("name", bean.getName());
            json.put("sortIndex", bean.getSortIndex());
            json.put("icon", ImageStorageTootl.getUrl(bean.getIcon()));
            json.put("downloadUrl", Config.getInstance().getString("toolbox.download.baseUrl") + "service/download.do?appId=" + bean.getId());
            json.put("downloadAuto", bean.getDownloadAuto() == 0 ? false : true);
            json.put("downloadCount", bean.getDownloadCount());
            json.put("detailUrl", bean.getDetailUrl());
            json.put("detailOpen", bean.getDetailOpen() == 0 ? false : true);
            json.put("rate", bean.getRate());
            json.put("extInfo1", bean.getExtInfo1());
            json.put("extInfo2", bean.getExtInfo2());
            json.put("extInfo3", bean.getExtInfo3());
            json.put("packageName", bean.getPackageName());
            json.put("versionCode", bean.getVersionCode());
            json.put("createDate", DateUtility.format(bean.getCreateDate()));
            json.put("updateDate", DateUtility.format(bean.getUpdateDate()));

            result.add(json);
        }
        try {
            redisToolboxService.set(rKey, result.toString());
        } catch (Exception e) {
            log.info(rKey + " to set redis error");

        }
        return SUCCESS;
    }

    public JSONArray getResult() {
        return result;
    }

    public void setResult(JSONArray result) {
        this.result = result;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

}
