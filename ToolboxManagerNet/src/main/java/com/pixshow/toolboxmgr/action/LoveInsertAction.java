package com.pixshow.toolboxmgr.action;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.support.BaseAction;
import com.pixshow.toolboxmgr.bean.LoveBean;
import com.pixshow.toolboxmgr.service.LoveService;
import com.pixshow.toolboxmgr.tools.ImageStorageTootl;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class LoveInsertAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private LoveService loveService;

    private String name;
    private String description;

    private String a;
    //--------
    private File image;
    private String imageFileName;

    //--------

    @Override
    @Action(value = "loveInsert", results = { @Result(name = SUCCESS, type = "redirectAction", location = "loveSearch") })
    public String execute() throws Exception {
        String extName = imageFileName.substring(imageFileName.lastIndexOf("."));
        imageFileName = UUID.randomUUID() + extName;
        ImageStorageTootl.upload(imageFileName, image);

        LoveBean bean = new LoveBean();
        bean.setName(name);
        bean.setImage(imageFileName);
        bean.setDescription(description);
        bean.setCreateDate(new Date());

        loveService.insertLove(bean);

        return SUCCESS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

}
