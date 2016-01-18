package com.pixshow.toolboxmgr.action;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.pixshow.framework.exception.api.DoNotCatchException;
import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.redis.RedisToolboxService;
import com.pixshow.toolboxmgr.bean.DiyboxBean;
import com.pixshow.toolboxmgr.service.DiyboxService;
import com.pixshow.toolboxmgr.service.DownloadService;
import com.pixshow.toolboxmgr.service.StatService;
import com.pixshow.toolboxmgr.service.ToolboxService;

@Controller
@Scope("prototype")
@Namespace("/service")
public class DownloadAction extends BaseAction {
    private static final long   serialVersionUID = 1L;
    // ///////////////////////////////////////////////////////////////
    @Autowired
    private DownloadService     downloadService;
    @Autowired
    private ToolboxService      toolboxService;
    @Autowired
    private DiyboxService       diyboxService;
    @Autowired
    private StatService         statService;
    @Autowired
    private RedisToolboxService redisToolboxService;
    // ///////////////////////////////////////////////////////////////
    private Integer             appId;
    private Integer             diyId;

    // ///////////////////////////////////////////////////////////////
    @Override
    @Action(value = "download", results = { @Result(name = SUCCESS, type = "redirect", location = "${downloadUrl}") })
    public String execute() throws Exception {
        try {
            String code = null;
            if (appId != null) {
                String rKey = "download@tb_toolbox_" + appId;
                String downloadUrl = redisToolboxService.get(rKey);
                if (StringUtility.isEmpty(downloadUrl)) {
                    downloadUrl = downloadService.getUrl("tb_toolbox", appId);
                    redisToolboxService.set(rKey, downloadUrl);
                }

//                                downloadService.addDownloadCount("tb_toolbox", appId);

                if (StringUtils.isEmpty(downloadUrl)) {
                    throw new DoNotCatchException();
                }
                ActionContext.getContext().put("downloadUrl", downloadUrl);

                //
                //                ToolboxBean toolbox = toolboxService.searchByIDTool(appId);
                //                code = toolbox != null ? toolbox.getPackageName() + "_toolbox_download" : null;
            }

            if (diyId != null) {
                downloadService.addDownloadCount("tb_diybox", diyId);
                String downloadUrl = downloadService.getUrl("tb_diybox", diyId);
                ActionContext.getContext().put("downloadUrl", downloadUrl);

                //
                DiyboxBean diybox = diyboxService.searchById(diyId);
                code = diybox != null ? diybox.getPackageName() + "_diybox_download" : null;
            }

            //
            //            if (StringUtility.isNotEmpty(code)) {
            //                statService.pvStat(code, 1, new Date());
            //            }
        } catch (Exception e) {
            throw new DoNotCatchException();
        }
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

}
