package com.pixshow.toolboxmgr.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pixshow.framework.support.BaseService;
import com.pixshow.toolboxmgr.bean.DiyboxBean;
import com.pixshow.toolboxmgr.dao.DiyboxDao;

@Service
public class DiyboxService extends BaseService {

    @Resource
    private DiyboxDao diyboxDao;

    /**
     * 查询工具
     */
    public List<DiyboxBean> searchDiys() {
        return diyboxDao.searchTool();
    }

    /**
     * 通过ID查询工具
     */
    public DiyboxBean searchById(int id) {
        return diyboxDao.searchById(id);
    }

}
