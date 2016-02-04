package com.pixshow.toolboxmgr.bean;

import java.util.Date;

import com.pixshow.framework.support.BaseBean;

public class GameSuspendBean extends BaseBean {
    private static final long serialVersionUID = 1L;

    private int               id;
    private int               suspendType;
    private int               useType;
    private String            content;
    private Date              createDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSuspendType() {
        return suspendType;
    }

    public void setSuspendType(int suspendType) {
        this.suspendType = suspendType;
    }

    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
