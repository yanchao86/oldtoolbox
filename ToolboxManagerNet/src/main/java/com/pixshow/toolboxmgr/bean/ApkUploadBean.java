package com.pixshow.toolboxmgr.bean;

import java.util.Date;

import com.pixshow.framework.support.BaseBean;

public class ApkUploadBean extends BaseBean {
    private static final long serialVersionUID = 1L;

    private int               id;
    private String            user;
    private String            fileName;
    private String            msg;
    private Date              createDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
