package com.pixshow.toolboxmgr.action;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.constant.GameSuspend;
import com.pixshow.framework.support.BaseAction;
import com.pixshow.toolboxmgr.bean.GameSuspendBean;
import com.pixshow.toolboxmgr.service.GameSuspendService;
import com.pixshow.toolboxmgr.service.ToolboxService;
import com.pixshow.toolboxmgr.tools.ImageStorageTootl;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class GameSuspendAction extends BaseAction {
    private static final long   serialVersionUID = 1L;
    @Autowired
    private GameSuspendService  gameSuspendService;
    @Autowired
    private ToolboxService      toolboxService;

    private int                 suspendType;
    private File                highPic;
    private String              highPicFileName;
    private File                widthPic;
    private String              widthPicFileName;
    private String              picUrl;
    private int                 picType;
    private List<String>        gameRecommend;
    private int                 id;

    private Map<String, Object> result           = new HashMap<String, Object>();

    @Action(value = "suspend", results = { @Result(name = SUCCESS, location = "/game/suspend.jsp") })
    public String suspend() {
        result.put("toolboxs", toolboxService.searchTool());
        result.put("gameSuspends", gameSuspendService.findsAll());
        return SUCCESS;
    }

    @Action(value = "saveSuspend", results = { @Result(name = SUCCESS, type = "redirectAction", location = "suspend") })
    public String saveSuspend() {
        if (suspendType == GameSuspend.suspendType.isPic) {
            GameSuspendBean bean = new GameSuspendBean();
            JSONObject content = new JSONObject();
            if (highPic != null && widthPic != null) {
                String highName = UUID.randomUUID() + highPicFileName.substring(highPicFileName.lastIndexOf("."));
                String high = ImageStorageTootl.upload(highName, highPic);
                String widthName = UUID.randomUUID() + widthPicFileName.substring(widthPicFileName.lastIndexOf("."));
                String width = ImageStorageTootl.upload(widthName, widthPic);
                content.put("highPic", high);
                content.put("widthPic", width);
                content.put("picUrl", picUrl);
                content.put("picType", picType);
                bean.setContent(content.toString());
                bean.setCreateDate(new Date());
                gameSuspendService.save(bean);
            }
        } else if (gameRecommend != null) {
            GameSuspendBean bean = new GameSuspendBean();
            bean.setContent(JSONArray.fromObject(gameRecommend).toString());
            bean.setUseType(GameSuspend.useType.unUse);
            bean.setSuspendType(suspendType);
            bean.setCreateDate(new Date());
            gameSuspendService.save(bean);
        }
        return SUCCESS;
    }

    @Action(value = "updatSuspendUseType", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String updatSuspendUseType() {
        result.put("result", gameSuspendService.updateUseType(id));
        return SUCCESS;
    }

    @Action(value = "deleteSuspend", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String deletesuspend() {
        gameSuspendService.delete(id);
        return SUCCESS;
    }

    @Action(value = "suspendRecycle", results = { @Result(name = SUCCESS, location = "/game/suspendRecycle.jsp") })
    public String suspendRecycle() {
        result.put("toolboxs", toolboxService.searchTool());
        result.put("gameSuspends", gameSuspendService.findsRecycle());
        return SUCCESS;
    }

    @Action(value = "restoreSuspend", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String restoresuspend() {
        gameSuspendService.restore(id);
        return SUCCESS;
    }

    private String sortIds;

    @Action(value = "suspendSort", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String suspendSort() {
        gameSuspendService.updateContent(id, "[" + sortIds + "]");
        return SUCCESS;
    }

    public int getSuspendType() {
        return suspendType;
    }

    public void setSuspendType(int suspendType) {
        this.suspendType = suspendType;
    }

    public File getHighPic() {
        return highPic;
    }

    public void setHighPic(File highPic) {
        this.highPic = highPic;
    }

    public String getHighPicFileName() {
        return highPicFileName;
    }

    public void setHighPicFileName(String highPicFileName) {
        this.highPicFileName = highPicFileName;
    }

    public File getWidthPic() {
        return widthPic;
    }

    public void setWidthPic(File widthPic) {
        this.widthPic = widthPic;
    }

    public String getWidthPicFileName() {
        return widthPicFileName;
    }

    public void setWidthPicFileName(String widthPicFileName) {
        this.widthPicFileName = widthPicFileName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getPicType() {
        return picType;
    }

    public void setPicType(int picType) {
        this.picType = picType;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getGameRecommend() {
        return gameRecommend;
    }

    public void setGameRecommend(List<String> gameRecommend) {
        this.gameRecommend = gameRecommend;
    }

    public String getSortIds() {
        return sortIds;
    }

    public void setSortIds(String sortIds) {
        this.sortIds = sortIds;
    }

}
