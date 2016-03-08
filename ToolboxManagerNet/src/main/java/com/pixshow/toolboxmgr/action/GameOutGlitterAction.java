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
import com.pixshow.toolboxmgr.bean.GameOutGlitterBean;
import com.pixshow.toolboxmgr.service.GameOutGlitterService;
import com.pixshow.toolboxmgr.tools.ImageStorageTootl;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class GameOutGlitterAction extends BaseAction {
    private static final long     serialVersionUID = 1L;
    @Autowired
    private GameOutGlitterService gameOutGlitterService;

    private File   picture;
    private String pictureFileName;
    private String buttonName;
    private int    buttonType;
    private String buttonUrl;
    private String startTime;
    private String endTime;
    private String packageName;
    private String versionCode;
    private int    indexNum;
    private int    id;

    private Map<String, Object> result = new HashMap<String, Object>();

    @Action(value = "outGlitter", results = { @Result(name = SUCCESS, location = "/game/outGlitter.jsp") })
    public String outGlitter() {
        result.put("gameGlitters", gameOutGlitterService.findsAll());
        return SUCCESS;
    }

    @Action(value = "saveOutGlitter", results = { @Result(name = SUCCESS, type = "redirectAction", location = "outGlitter") })
    public String saveOutGlitter() {
        String pictureName = UUID.randomUUID() + pictureFileName.substring(pictureFileName.lastIndexOf("."));
        String pictureUrl = ImageStorageTootl.upload(pictureName, picture);

        GameOutGlitterBean bean = new GameOutGlitterBean();
        bean.setButtonName(buttonName);
        bean.setButtonType(buttonType);
        bean.setButtonUrl(buttonUrl);
        bean.setCreateDate(new Date());
        bean.setEndTime(DateUtility.parseDate(endTime, "yyyy-MM-dd"));
        bean.setStartTime(DateUtility.parseDate(startTime, "yyyy-MM-dd"));
        bean.setPicture(pictureUrl);
        bean.setUseType(GameGlitter.useType.unUse);
        bean.setIndexNum(indexNum);
        bean.setPackageName(packageName);
        bean.setVersionCode(versionCode);
        gameOutGlitterService.save(bean);
        return SUCCESS;
    }

    @Action(value = "updateOutGlitterPage", results = { @Result(name = SUCCESS, location = "/game/updateOutGlitter.jsp") })
    public String updateOutGlitterPage() {
        result.put("gameGlitter", gameOutGlitterService.findById(id));
        return SUCCESS;
    }

    @Action(value = "updateOutGlitter", results = { @Result(name = SUCCESS, type = "redirectAction", location = "outGlitter") })
    public String updateOutGlitter() {
        GameOutGlitterBean bean = gameOutGlitterService.findById(id);

        String pictureName = "";
        String pictureUrl = bean.getPicture();
        if (picture != null) {
            pictureName = UUID.randomUUID() + pictureFileName.substring(pictureFileName.lastIndexOf("."));
            pictureUrl = ImageStorageTootl.upload(pictureName, picture);
        }

        bean.setButtonName(buttonName);
        bean.setButtonType(buttonType);
        bean.setButtonUrl(buttonUrl);
        bean.setEndTime(DateUtility.parseDate(endTime, "yyyy-MM-dd"));
        bean.setStartTime(DateUtility.parseDate(startTime, "yyyy-MM-dd"));
        bean.setPicture(pictureUrl);
        bean.setIndexNum(indexNum);
        bean.setPackageName(packageName);
        bean.setVersionCode(versionCode);
        gameOutGlitterService.update(bean);
        return SUCCESS;
    }

    @Action(value = "updatOutGlitterUseType", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String updatOutGlitterUseType() {
        result.put("result", gameOutGlitterService.updateUseType(id));
        return SUCCESS;
    }

    @Action(value = "deleteOutGlitter", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String deleteOutGlitter() {
        gameOutGlitterService.delete(id);
        return SUCCESS;
    }

    @Action(value = "outGlitterRecycle", results = { @Result(name = SUCCESS, location = "/game/outGlitterRecycle.jsp") })
    public String outGlitterRecycle() {
        result.put("gameGlitters", gameOutGlitterService.findsRecycle());
        return SUCCESS;
    }

    @Action(value = "restoreOutGlitter", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
    public String restoreOutGlitter() {
        gameOutGlitterService.restore(id);
        return SUCCESS;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public File getPicture() {
        return picture;
    }

    public void setPicture(File picture) {
        this.picture = picture;
    }

    public String getPictureFileName() {
        return pictureFileName;
    }

    public void setPictureFileName(String pictureFileName) {
        this.pictureFileName = pictureFileName;
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

    public int getIndexNum() {
        return indexNum;
    }

    public void setIndexNum(int indexNum) {
        this.indexNum = indexNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

}
