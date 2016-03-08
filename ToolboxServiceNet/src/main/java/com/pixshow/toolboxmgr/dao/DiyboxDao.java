package com.pixshow.toolboxmgr.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.toolboxmgr.bean.DiyboxBean;
import com.pixshow.toolboxmgr.bean.ToolboxBean;

@Repository
public class DiyboxDao extends BaseDao {

    /**
     * 查询工具
     */
    public List<DiyboxBean> searchTool() {
        return queryForList("SELECT * FROM tb_diybox ORDER BY sortIndex", DiyboxBean.class);
    }

    /**
     * 通过ID查询工具
     */
    public DiyboxBean searchById(int id) {
        return queryForBean("SELECT * from tb_diybox WHERE id=?", DiyboxBean.class, id);
    }

}
