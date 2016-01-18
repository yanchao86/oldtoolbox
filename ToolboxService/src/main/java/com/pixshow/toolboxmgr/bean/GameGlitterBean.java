package com.pixshow.toolboxmgr.bean;

import java.util.Date;

import com.pixshow.framework.support.BaseBean;

public class GameGlitterBean extends BaseBean {
    private static final long serialVersionUID = 1L;
    private int               id;
    private String            highPic;
    private String            widthPic;
    private int               dwellTime;
    private String            buttonName;
    private int               buttonType;
    private String            buttonUrl;
    private Date              startTime;
    private Date              endTime;
    private Date              createDate;
    private int               useType;
    private int               glitterType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHighPic() {
        return highPic;
    }

    public void setHighPic(String highPic) {
        this.highPic = highPic;
    }

    public String getWidthPic() {
        return widthPic;
    }

    public void setWidthPic(String widthPic) {
        this.widthPic = widthPic;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getGlitterType() {
        return glitterType;
    }

    public void setGlitterType(int glitterType) {
        this.glitterType = glitterType;
    }

    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }

}
