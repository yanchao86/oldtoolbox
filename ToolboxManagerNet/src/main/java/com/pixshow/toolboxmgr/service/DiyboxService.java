package com.pixshow.toolboxmgr.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.framework.support.BaseService;
import com.pixshow.toolboxmgr.bean.DiyboxBean;
import com.pixshow.toolboxmgr.bean.DlDayStatBean;
import com.pixshow.toolboxmgr.dao.DiyboxDao;

@Service
public class DiyboxService extends BaseService {

    @Resource
    private DiyboxDao diyboxDao;

    /**
     * 添加工具
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void insertTool(DiyboxBean bean) {
        bean.setId(diyboxDao.insertTool(bean));
        diyboxDao.insertPackage(bean.getId(), bean.getPackageName(), bean.getCreateDate());
    }

    /**
     * 查询工具
     */
    public List<DiyboxBean> searchTool() {
        return diyboxDao.searchTool();
    }

    /**
     * 修改工具
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateTool(DiyboxBean bean) {
        diyboxDao.updateTool(bean);
        diyboxDao.updatePackage(bean.getId(), bean.getPackageName());
    }

    /**
     * 通过ID查询工具
     */
    public DiyboxBean searchByIDTool(int id) {
        return diyboxDao.searchByIDTool(id);
    }

    /**
     * 删除工具
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteTool(int id) {
        diyboxDao.insertRecycle(diyboxDao.searchByIDTool(id));
        diyboxDao.deleteTool(id);

    }

    /**
     * 工具排序
     */
    public void sortTool(int id, int sortIndex) {
        diyboxDao.sortTool(id, sortIndex);
    }
    
    /**
     * 小工具日下载量统计
     * @param toolId
     *
     */
    public List<DlDayStatBean> dlDayStats(int toolId) {
       return diyboxDao.dlDayStats(toolId);
    }
}
