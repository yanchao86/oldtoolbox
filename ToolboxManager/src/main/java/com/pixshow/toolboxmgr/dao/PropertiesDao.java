package com.pixshow.toolboxmgr.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jf.smali.smaliParser.integer_literal_return;
import org.python.antlr.PythonParser.return_stmt_return;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.SqlUtility;

@Repository
public class PropertiesDao extends BaseDao {
	public List<Map<String, Object>> getIBoxOpen(int key){
		return getJdbcTemplate().queryForList("SELECT day,code, SUM(count) counts FROM tb_toolbox_day_stat WHERE day=? AND code LIKE '%ibox_open' GROUP BY code ORDER BY SUM(count) DESC", key);
	}
	public List<Map<String, Object>> getNotificationPV(int key){
		return getJdbcTemplate().queryForList("SELECT day,code, SUM(count) counts FROM tb_toolbox_day_stat WHERE day=? AND code LIKE '%notifcation_ibox_pv' GROUP BY code ORDER BY SUM(count) DESC", key);
	}
	public List<Map<String, Object>> getNotificationUV(int key){
		return getJdbcTemplate().queryForList("SELECT day,code, SUM(count) counts FROM tb_toolbox_day_stat WHERE day=? AND code LIKE '%notifcation_ibox_uv' GROUP BY code ORDER BY SUM(count) DESC", key);
	}
	public List<Map<String, Object>> getAppsPV(int key){
		return getJdbcTemplate().queryForList("SELECT day,code, SUM(count) counts FROM tb_toolbox_day_stat WHERE day=? AND code LIKE '%apps_ibox_pv' GROUP BY code ORDER BY SUM(count) DESC", key);
	}
	public List<Map<String, Object>> getAppsUV(int key){
		return getJdbcTemplate().queryForList("SELECT day,code, SUM(count) counts FROM tb_toolbox_day_stat WHERE day=? AND code LIKE '%apps_ibox_uv' GROUP BY code ORDER BY SUM(count) DESC", key);
	}
    public String getValue(String key) {
        List<String> values = getJdbcTemplate().queryForList("select value from tb_properties where type = ?", String.class, key);
        return (values == null || values.size() == 0) ? null : values.get(0);
    }

    public List<Map<String, Object>> likeValue(String like) {
        return getJdbcTemplate().queryForList("select * from tb_properties where type like ?", like + "%");
    }
    
    public void setValue(String key, String value) {
        int count = getJdbcTemplate().queryForInt("select count(*) counts from tb_properties where type = ?", key);
        if (count == 0) {
            getJdbcTemplate().update("insert into tb_properties(type,value) values(?,?)", key, value);
        } else {
            getJdbcTemplate().update("update tb_properties set value = ? where type =?", value, key);
        }
    }

    public List<Map<String, Object>> getValues(String key) {
        List<Map<String, Object>> values = getJdbcTemplate().queryForList("select * from tb_properties where type like ?", key + "%");
        return values;
    }

    public void updateValue(String key, String value) {
        getJdbcTemplate().update("update tb_properties set value=? where type=?", value, key);
    }

    public void delchannel(String key) {
        getJdbcTemplate().update("delete from tb_properties where type=?", key);
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
}
