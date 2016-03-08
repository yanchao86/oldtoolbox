package com.pixshow.toolboxmgr.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.SqlUtility;

@Repository
public class CustomDao extends BaseDao {

    public String getConfig(String key) {
        return getJdbcTemplate().queryForObject("select value from tb_custom_cfg_def where code = ?", String.class, key);
    }

    public List<String> getConfig(String... keys) {
        String sql = "select * from tb_custom_cfg_def w where " + SqlUtility.in("w.code", keys.length);
        List<Map<String, Object>> rs = getJdbcTemplate().queryForList(sql, (Object[]) keys);
        Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
        for (Map<String, Object> value : rs) {
            map.put((String) value.get("code"), value);
        }
        List<String> list = new ArrayList<String>();
        for (String key : keys) {
            if (map.get(key) != null) {
                list.add((String) map.get(key).get("definition"));
            } else {
                list.add(null);
            }
        }
        return list;
    }

    public String getValue(String key) {
        return getJdbcTemplate().queryForObject("select value from tb_custom_cfg_val where code = ?", String.class, key);
    }

    public List<String> getValue(String... keys) {
        String sql = "select * from tb_custom_cfg_val w where " + SqlUtility.in("w.code", keys.length);
        List<Map<String, Object>> rs = getJdbcTemplate().queryForList(sql, (Object[]) keys);
        Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
        for (Map<String, Object> value : rs) {
            map.put((String) value.get("code"), value);
        }
        List<String> list = new ArrayList<String>();
        for (String key : keys) {
            if (map.get(key) != null) {
                list.add((String) map.get(key).get("value"));
            } else {
                list.add(null);
            }
        }
        return list;
    }

    public void setValue(String key, String value) {
        int count = getJdbcTemplate().queryForInt("select count(*) counts from tb_custom_cfg_val where w.code = ?", key);
        if (count == 0) {
            getJdbcTemplate().update("insert into tb_custom_cfg_val(code,value) values(?,?)", key, value);
        } else {
            getJdbcTemplate().update("update tb_custom_cfg_val set value = ? where code =?", value, key);
        }
    }

}
