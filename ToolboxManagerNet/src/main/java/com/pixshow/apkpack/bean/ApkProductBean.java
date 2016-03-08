package com.pixshow.apkpack.bean;

import java.util.Date;

import com.pixshow.framework.support.BaseBean;

public class ApkProductBean extends BaseBean {
    private static final long serialVersionUID = 1L;

    private int               id;
    private String            name;
    private String            engName;
    private int               apkKeyId;
    private String            appKey;
    private Date              createDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public int getApkKeyId() {
        return apkKeyId;
    }

    public void setApkKeyId(int apkKeyId) {
        this.apkKeyId = apkKeyId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

}
