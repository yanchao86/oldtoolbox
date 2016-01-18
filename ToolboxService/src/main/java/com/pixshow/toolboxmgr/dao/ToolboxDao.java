package com.pixshow.toolboxmgr.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.SqlUtility;
import com.pixshow.toolboxmgr.bean.ToolboxBean;

@Repository
public class ToolboxDao extends BaseDao {

    /**
     * 查询工具
     */
    public List<ToolboxBean> searchTool(int index, int items) {
        return queryForList("SELECT * FROM tb_toolbox where sortIndex>? ORDER BY sortIndex limit ?", ToolboxBean.class, index, items);
    }

    /**
     * 通过ID查询工具
     */
    public ToolboxBean searchByIDTool(int id) {
        return queryForBean("SELECT * from tb_toolbox WHERE id=?", ToolboxBean.class, id);
    }

    public List<ToolboxBean> searchToolByIds(List<Integer> ids) {
        return queryForList("SELECT * FROM tb_toolbox where " + SqlUtility.in("id", ids.size()), ToolboxBean.class, ids.toArray());
    }

    public List<Map<String, Object>> allPackageName() {
        return getJdbcTemplate().queryForList("SELECT packageName,icon FROM tb_toolbox");
    }
}
