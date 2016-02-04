package com.pixshow.toolboxmgr.bean;

import java.util.Date;

import com.pixshow.framework.support.BaseBean;

public class GameRecommendBean extends BaseBean {
    private static final long serialVersionUID = 1L;

    private int               id;
    private int               useType;
    private String            content;
    private Date              createDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }

}
