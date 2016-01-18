package com.pixshow.toolboxmgr.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pixshow.toolboxmgr.bean.ToolboxPackageBean;
import com.pixshow.toolboxmgr.dao.ToolboxPackageDao;

@Service
public class ToolboxPackageService {
    @Resource
    private ToolboxPackageDao packageDao;

    /**
     * 查询包名表
     */
    public List<ToolboxPackageBean> searchPackage() {
        return packageDao.searchPackage();
    }
}
