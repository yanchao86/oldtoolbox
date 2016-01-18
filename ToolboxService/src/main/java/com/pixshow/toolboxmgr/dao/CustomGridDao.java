package com.pixshow.toolboxmgr.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.SqlUtility;

@Repository
public class CustomGridDao extends BaseDao {

    public static final String grid_table_prefix = "tb_custom_grid_val_";

    public List<Map<String, Object>> getData(String gridTble, List<Object> ids) {
        if (ids == null || ids.size() == 0) {
            return getJdbcTemplate().queryForList("select * from " + gridTble);
        } else {
            return getJdbcTemplate().queryForList("select * from " + gridTble + " where " + SqlUtility.in("id", ids.size()), ids.toArray());
        }
    }

    public String gridConfig(String gridTble) {
        List<Map<String, Object>> list = getJdbcTemplate().queryForList("select definition from tb_custom_grid_def where code=?", gridTble);
        return list.size() > 0 ? (String) list.get(0).get("definition") : null;
    }

    public List<Map<String, Object>> customGridSql(String sql) {
        return getJdbcTemplate().queryForList(sql);
    }
}
