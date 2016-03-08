package com.pixshow.toolboxmgr.action;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.constant.GameGlitter;
import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.DateUtility;
import com.pixshow.toolboxmgr.bean.GameGlitterBean;
import com.pixshow.toolboxmgr.service.GameGlitterService;
import com.pixshow.toolboxmgr.tools.ImageStorageTootl;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class GameGlitterAction extends BaseAction {
    private static final long   serialVersionUID = 1L;
    @Autowired
    private GameGlitterService  gameGlitterService;

    private int                 glitterType;
    private File                highPic;
    private String              highPicFileName;
    private File                widthPic;
    private String              widthPicFileName;
    private int                 dwellTime;
    private String              buttonName;
    private int                 buttonType;
    private String              buttonUrl;
    private String              startTime;
    private String              endTime;
    private int                 id;

    private Map<String, Object> result           = new HashMap<String, Object>();

    @Action(value = "glitter", results = { @Result(name = SUCCESS, location = "/game/glitter.jsp") })
    public String glitter() {
        result.put("gameGlitters", gameGlitterService.findsAll());
        return SUCCESS;
    }

    @Action(value = "saveGlitter", results = { @Result(name = SUCCESS, type = "redirectAction", location = "glitter") })
    public String saveGlitter() {
        String highName = UUID.randomUUID() + highPicFileName.substring(highPicFileName.lastIndexOf("."));
        String high = ImageStorageTootl.upload(highName, highPic);
        String widthName = UUID.randomUUID() + widthPicFileName.substring(widthPicFileName.lastIndexOf("."));
        String width = ImageStorageTootl.upload(widthName, widthPic);

        GameGlitterBean bean = new GameGlitterBean();
        bean.setButtonName(buttonName);
        bean.setButtonType(buttonType);
        bean.setButtonUrl(buttonUrl);
        bean.setCreateDate(new Date());
        bean.setDwellTime(dwellTime);
        bean.setEndTime(DateUtility.parseDate(endTime, "yyyy-MM-dd"));
        bean.setGlitterType(glitterType);
        bean.setStartTime(DateUtility.parseDate(startTime, "yyyy-MM-dd"));
        bean.setHighPic(high);
        bean.setWidthPic(width);
        bean.setUseType(GameGlitter.useType.unUse);
        gameGlitterService.save(bean);
        return SUCCESS;
    }

    @Action(value = "updatGlitterUseType", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String updatGlitterUseType() {
        result.put("result", gameGlitterService.updateUseType(id));
        return SUCCESS;
    }

    @Action(value = "deleteGlitter", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String deleteGlitter() {
        gameGlitterService.delete(id);
        return SUCCESS;
    }

    @Action(value = "glitterRecycle", results = { @Result(name = SUCCESS, location = "/game/glitterRecycle.jsp") })
    public String glitterRecycle() {
        result.put("gameGlitters", gameGlitterService.findsRecycle());
        return SUCCESS;
    }

    @Action(value = "restoreGlitter", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String restoreGlitter() {
        gameGlitterService.restore(id);
        return SUCCESS;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGlitterType() {
        return glitterType;
    }

    public void setGlitterType(int glitterType) {
        this.glitterType = glitterType;
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

    public int getDwellTime() {
        return dwellTime;
    }

    public void setDwellTime(int dwellTime) {
        this.dwellTime = dwellTime;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public int getButtonType() {
        return buttonType;
    }

    public void setButtonType(int buttonType) {
        this.buttonType = buttonType;
    }

    public String getButtonUrl() {
        return buttonUrl;
    }

    public void setButtonUrl(String buttonUrl) {
        this.buttonUrl = buttonUrl;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

}
