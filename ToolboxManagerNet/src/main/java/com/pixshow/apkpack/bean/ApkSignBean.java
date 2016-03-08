package com.pixshow.apkpack.bean;

import java.util.Date;

import com.pixshow.framework.support.BaseBean;

public class ApkSignBean extends BaseBean {
    private static final long serialVersionUID = 1L;

    private int               id;
    private String            name;
    private int               productId;
    private int               apkKeyId;
    private String            versioncode;
    private String            versionname;
    private String            signApkFile;
    private String            uploadUser;
    private String            uploadIp;
    private String            msg;
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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getApkKeyId() {
        return apkKeyId;
    }

    public void setApkKeyId(int apkKeyId) {
        this.apkKeyId = apkKeyId;
    }

    public String getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(String versioncode) {
        this.versioncode = versioncode;
    }

    public String getVersionname() {
        return versionname;
    }

    public void setVersionname(String versionname) {
        this.versionname = versionname;
    }

    public String getSignApkFile() {
        return signApkFile;
    }

    public void setSignApkFile(String signApkFile) {
        this.signApkFile = signApkFile;
    }

    public String getUploadUser() {
        return uploadUser;
    }

    public void setUploadUser(String uploadUser) {
        this.uploadUser = uploadUser;
    }

    public String getUploadIp() {
        return uploadIp;
    }

    public void setUploadIp(String uploadIp) {
        this.uploadIp = uploadIp;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
