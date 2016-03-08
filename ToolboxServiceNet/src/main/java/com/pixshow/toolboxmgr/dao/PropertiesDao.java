package com.pixshow.toolboxmgr.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.SqlUtility;

@Repository
public class PropertiesDao extends BaseDao {

    public String getValue(String key) {
        return getJdbcTemplate().queryForObject("select value from tb_properties where type = ?", String.class, key);
    }

    public List<Map<String, Object>> likeValue(String like) {
        return getJdbcTemplate().queryForList("select * from tb_properties where type like ?", like + "%");
    }

    public List<String> getValue(String... keys) {
        String sql = "select * from tb_properties w where " + SqlUtility.in("w.type", keys.length);
        List<Map<String, Object>> rs = getJdbcTemplate().queryForList(sql, (Object[]) keys);
        Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
        for (Map<String, Object> value : rs) {
            map.put((String) value.get("type"), value);
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
        int count = getJdbcTemplate().queryForInt("select count(*) counts from tb_properties where type = ?", key);
        if (count == 0) {
            getJdbcTemplate().update("insert into tb_properties(type,value) values(?,?)", key, value);
        } else {
            getJdbcTemplate().update("update tb_properties set value = ? where type =?", value, key);
        }
    }

}
