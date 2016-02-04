package com.pixshow.custom.grid;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.StringUtility;

@Repository
public class GridDataDao extends BaseDao {
    public static final String TABLE_PREFIX = "tb_custom_grid_val_";

    public List<Map<String, Object>> getData(String code, String order) {
        order = StringUtility.isEmpty(order) ? "asc" : order;
        return getJdbcTemplate().queryForList("select * from " + TABLE_PREFIX + code + " order by id " + order);
    }

    public void insert(String table, String sql, Object... values) {
        insert("insert into " + TABLE_PREFIX + table + " " + sql, values);
    }

    public void delete(String table, int id) {
        update("delete from " + TABLE_PREFIX + table + " where id=?", id);
    }

    public void update(String table, String update, int id) {
        update("update " + TABLE_PREFIX + table + " set " + update + " where id=?", id);
    }

    public List<Map<String, Object>> gridData(String grid) {
        return getJdbcTemplate().queryForList("SELECT * FROM " + grid);
    }

    public List<Map<String, Object>> customGridData(String grid, String filters) {
        return getJdbcTemplate().queryForList("SELECT " + filters + " FROM " + TABLE_PREFIX + grid);
    }
}
