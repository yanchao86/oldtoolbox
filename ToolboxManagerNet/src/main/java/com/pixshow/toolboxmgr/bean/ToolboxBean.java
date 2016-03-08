package com.pixshow.toolboxmgr.bean;

import java.util.Date;

import com.pixshow.framework.support.BaseBean;

public class ToolboxBean extends BaseBean {
    private static final long serialVersionUID = 1L;
    private int               id;
    private String            name;
    private int               sortIndex;
    private String            icon;
    private String            downloadUrl;
    private int               downloadAuto;
    private int               downloadCount;
    private String            detailUrl;
    private int               detailOpen;
    private float             rate;
    private String            packageName;
    private String            versionCode;
    private String            extInfo1;
    private String            extInfo2;
    private String            extInfo3; //JSON 对象
    private Date              createDate;
    private Date              updateDate;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public int getDownloadAuto() {
        return downloadAuto;
    }

    public void setDownloadAuto(int downloadAuto) {
        this.downloadAuto = downloadAuto;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public int getDetailOpen() {
        return detailOpen;
    }

    public void setDetailOpen(int detailOpen) {
        this.detailOpen = detailOpen;
    }

    public String getExtInfo1() {
        return extInfo1;
    }

    public void setExtInfo1(String extInfo1) {
        this.extInfo1 = extInfo1;
    }

    public String getExtInfo2() {
        return extInfo2;
    }

    public void setExtInfo2(String extInfo2) {
        this.extInfo2 = extInfo2;
    }

    public String getExtInfo3() {
        return extInfo3;
    }

    public void setExtInfo3(String extInfo3) {
        this.extInfo3 = extInfo3;
    }

}
