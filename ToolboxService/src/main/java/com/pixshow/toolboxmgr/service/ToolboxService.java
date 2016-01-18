package com.pixshow.toolboxmgr.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pixshow.framework.support.BaseService;
import com.pixshow.toolboxmgr.bean.ToolboxBean;
import com.pixshow.toolboxmgr.dao.ToolboxDao;

@Service
public class ToolboxService extends BaseService {

    @Resource
    private ToolboxDao toolboxDao;

    /**
     * 查询工具
     */
    public List<ToolboxBean> searchTool(int index, int items) {
        return toolboxDao.searchTool(index, items);
    }

    /**
     * 通过ID查询工具
     */
    public ToolboxBean searchByIDTool(int id) {
        return toolboxDao.searchByIDTool(id);
    }
    
    public List<ToolboxBean> searchToolByIds(List<Integer> ids) {
        return toolboxDao.searchToolByIds(ids);
    }
    
    public List<Map<String, Object>> allPackageName() {
        return toolboxDao.allPackageName();
    }

}
