package com.pixshow.apkpack.bean;

import java.util.Date;

import com.pixshow.framework.support.BaseBean;

public class ApkChannelBean extends BaseBean {
    private static final long serialVersionUID = 1L;

    private int               id;
    private String            name;
    private String            channel;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
