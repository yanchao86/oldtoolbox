package com.pixshow.toolboxmgr.bean;

import java.util.Date;

public class ToolboxPackageBean {
    private int toolboxId;
    private String packageName;
    private Date createDate;

    public int getToolboxId() {
        return toolboxId;
    }

    public void setToolboxId(int toolboxId) {
        this.toolboxId = toolboxId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
