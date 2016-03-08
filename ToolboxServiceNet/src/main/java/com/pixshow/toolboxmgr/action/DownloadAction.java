package com.pixshow.toolboxmgr.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.pixshow.framework.support.BaseAction;
import com.pixshow.redis.RedisToolboxService;
import com.pixshow.toolboxmgr.bean.ToolboxBean;
import com.pixshow.toolboxmgr.service.ToolboxService;

import net.sf.json.JSONObject;

@Controller
@Scope("prototype")
@Namespace("/service")
public class DownloadAction extends BaseAction {
    private static final long   serialVersionUID = 1L;
    // ///////////////////////////////////////////////////////////////
    @Autowired
    private ToolboxService      toolboxService;
    @Autowired
    private RedisToolboxService redisToolboxService;
    // ///////////////////////////////////////////////////////////////
    private Integer             appId;
    private Integer             diyId;

    // ///////////////////////////////////////////////////////////////
    @Override
    @Action(value = "download", results = { @Result(name = SUCCESS, type = "redirect", location = "${downloadUrl}") })
    public String execute() throws Exception {
        String appKey = "tb_toolbox@" + appId;
        JSONObject app = new JSONObject();
        if (redisToolboxService.check(appKey)) {
            String appStr = redisToolboxService.get(appKey);
            app = JSONObject.fromObject(appStr);
        } else {
            ToolboxBean toolbox = toolboxService.searchByIDTool(appId);
            app = JSONObject.fromObject(toolbox);
            redisToolboxService.set(appKey, app.toString());
        }
        String downloadUrl = app.getString("downloadUrl");
        String code = app.getString("packageName") + "_toolbox_download";
        ActionContext.getContext().put("downloadUrl", downloadUrl);
        //redis 统计
        redisToolboxService.downLoadStat(appId, null, code);
        return SUCCESS;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getDiyId() {
        return diyId;
    }

    public void setDiyId(Integer diyId) {
        this.diyId = diyId;
    }

    /**
            if (diyId != null) {
                String downloadUrl = null;
                String rKey = "download@diyId_url_" + diyId;
                if (redisToolboxService.check(rKey)) {
                    downloadUrl = redisToolboxService.get(rKey);
                } else {
                    downloadUrl = downloadService.getUrl("tb_diybox", diyId);
                    redisToolboxService.set(rKey, downloadUrl);
                }
    
                ActionContext.getContext().put("downloadUrl", downloadUrl);
                //
                String dyKey = "download@diyId_" + diyId;
                if (redisToolboxService.check(dyKey)) {
                    code = redisToolboxService.get(dyKey);
                } else {
                    DiyboxBean diybox = diyboxService.searchById(diyId);
                    code = diybox != null ? diybox.getPackageName() + "_diybox_download" : null;
                    redisToolboxService.set(dyKey, code);
                }
    
            }
     */

}
