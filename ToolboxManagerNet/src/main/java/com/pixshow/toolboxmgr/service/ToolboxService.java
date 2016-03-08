package com.pixshow.toolboxmgr.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.framework.support.BaseService;
import com.pixshow.toolboxmgr.bean.DlDayStatBean;
import com.pixshow.toolboxmgr.bean.ToolboxBean;
import com.pixshow.toolboxmgr.dao.ToolboxDao;

@Service
public class ToolboxService extends BaseService {

    @Resource
    private ToolboxDao toolboxDao;

    /**
     * 添加工具
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void insertTool(ToolboxBean bean) {
        bean.setId(toolboxDao.insertTool(bean));
        toolboxDao.insertPackage(bean.getId(), bean.getPackageName(), bean.getCreateDate());
    }

    /**
     * 查询工具
     */
    public List<ToolboxBean> searchTool() {
        return toolboxDao.searchTool();
    }
    /**
     * 查询工具
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public int maxSortIndex() {
        return toolboxDao.maxSortIndex();
    }

    /**
     * 修改工具
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateTool(ToolboxBean bean) {
        toolboxDao.updateTool(bean);
        toolboxDao.updatePackage(bean.getId(), bean.getPackageName());
    }

    /**
     * 通过ID查询工具
     */
    public ToolboxBean searchByIDTool(int id) {
        return toolboxDao.searchByIDTool(id);
    }

    /**
     * 删除工具
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteTool(int id) {
        toolboxDao.insertRecycle(toolboxDao.searchByIDTool(id));
        toolboxDao.deleteTool(id);

    }

    /**
     * 工具排序
     */
    public void sortTool(int id, int sortIndex) {
        toolboxDao.sortTool(id, sortIndex);
    }

    /**
     * 小工具日下载量统计
     * 
     * @param toolId
     * 
     */
    public List<DlDayStatBean> dlDayStats(int toolId) {
        return toolboxDao.dlDayStats(toolId);
    }

    /**
     * 返回所有回收站里的内容
     * 
     * @return
     */
    public List<ToolboxBean> findAllRecycle() {
        return toolboxDao.findAllRecycle();
    }

    /**
     * 根据ID还原回收站
     * 
     * @param id
     */
    public void setlctInto(int id) {
        toolboxDao.setlctInto(id);
    }
}
