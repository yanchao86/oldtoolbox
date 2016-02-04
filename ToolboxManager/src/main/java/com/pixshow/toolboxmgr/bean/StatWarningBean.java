package com.pixshow.toolboxmgr.bean;

import java.util.Date;

import com.pixshow.framework.support.BaseBean;

public class StatWarningBean extends BaseBean {
    private static final long serialVersionUID = 1L;

    private int               id;
    private String            code;
    private String            email;
    private double            upThreshold;
    private double            downThreshold;
    private Date              createDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getUpThreshold() {
        return upThreshold;
    }

    public void setUpThreshold(double upThreshold) {
        this.upThreshold = upThreshold;
    }

    public double getDownThreshold() {
        return downThreshold;
    }

    public void setDownThreshold(double downThreshold) {
        this.downThreshold = downThreshold;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
