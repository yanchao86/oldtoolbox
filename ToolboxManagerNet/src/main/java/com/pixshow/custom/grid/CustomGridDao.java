/*
 * Copyright (c) 2010-2014 www.pixshow.net All Rights Reserved
 *
 * File:CustomConfigDao.java Project: ToolboxManager
 * 
 * Creator:4399-lvtu-8 
 * Date:2014年3月7日 上午4:15:37
 * 
 */
package com.pixshow.custom.grid;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$
 * @since 2014年3月7日
 */
@Repository
public class CustomGridDao extends BaseDao {
    public static final String TABLE_PREFIX = "tb_custom_grid_val_";

    public void saveGrid(String code, String name, String definition) {
        insert("insert into tb_custom_grid_def (code,name,definition,updateDate,createDate) values (?, ?, ?, ?, ?)", code, name, definition, new Date(), new Date());
    }

    public void updateGrid(String code, String name, String definition) {
        update("update tb_custom_grid_def set name=?, definition=?, updateDate=? where code=?", name, definition, new Date(), code);
    }

    public Map<String, Object> findGridByCode(String code) {
        List<Map<String, Object>> list = getJdbcTemplate().queryForList("select * from tb_custom_grid_def where code=?", code);
        return list.size() > 0 ? list.get(0) : null;
    }

    public List<Map<String, Object>> gridCodeList() {
        return getJdbcTemplate().queryForList("select * from tb_custom_grid_def order by createDate desc");
    }

    public List<Map<String, Object>> gridList() {
        return getJdbcTemplate().queryForList("select * from tb_custom_grid_def order by createDate desc");
    }

    public void createTable(String definition) {
        JSONObject def = JSONObject.fromObject(definition);
        JSONArray pros = def.getJSONArray("properties");

        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE " + TABLE_PREFIX + def.getString("key") + " (");
        sql.append("`id` int(12) NOT NULL AUTO_INCREMENT");

        for (int i = 0; i < pros.size(); i++) {
            sql.append(" , ");

            JSONObject pro = pros.getJSONObject(i);
            String key = pro.getString("key");
            String name = pro.getString("name");
            String type = pro.getString("type");
            if ("0".equals(type)) {
                sql.append(key + " int(12) DEFAULT '0' COMMENT '" + name + "'");
            } else if ("1".equals(type) || "5".equals(type)) {
                sql.append(key + " varchar(1000) DEFAULT NULL COMMENT '" + name + "'");
            } else if ("2".equals(type)) {
                sql.append(key + " datetime DEFAULT NULL COMMENT '" + name + "'");
            } else if ("3".equals(type)) {
                sql.append(key + " tinyint(2) DEFAULT '0' COMMENT '" + name + "'");
            } else if ("4".equals(type)) {
                sql.append(key + " int(12) DEFAULT '0' COMMENT '" + name + "'");
            }
        }

        sql.append(", PRIMARY KEY (id)");
        sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='" + def.getString("name") + "'");
        getJdbcTemplate().execute(sql.toString());
    }

    public void alterTable(String oldDefinition, String definition) {
        JSONObject oldDef = JSONObject.fromObject(oldDefinition);
        JSONArray oldPros = oldDef.getJSONArray("properties");
        Map<String, String> oldKey = new HashMap<String, String>();
        for (int i = 0; i < oldPros.size(); i++) {
            JSONObject pro = oldPros.getJSONObject(i);
            String key = pro.getString("key");
            oldKey.put(key, key);
        }

        JSONObject def = JSONObject.fromObject(definition);
        JSONArray pros = def.getJSONArray("properties");

        StringBuilder sql = new StringBuilder();
        for (int i = 0; i < pros.size(); i++) {
            JSONObject pro = pros.getJSONObject(i);
            String key = pro.getString("key");
            String name = pro.getString("name");
            String type = pro.getString("type");
            if (oldKey.containsKey(key)) {
                continue;
            }
            if (sql.length() > 0) {
                sql.append("; ");
            }
            sql.append("ALTER TABLE " + TABLE_PREFIX + def.getString("key") + " ADD COLUMN " + key);
            if ("0".equals(type)) {
                sql.append(" int(12) DEFAULT '0' ");
            } else if ("1".equals(type) || "5".equals(type)) {
                sql.append(" varchar(1000) DEFAULT NULL ");
            } else if ("2".equals(type)) {
                sql.append(" datetime DEFAULT NULL ");
            } else if ("3".equals(type)) {
                sql.append(" tinyint(2) DEFAULT '0' ");
            } else if ("4".equals(type)) {
                sql.append(" int(12) DEFAULT '0' ");
            }
            sql.append("COMMENT '" + name + "'");
        }
        if (sql.length() > 0) {
            getJdbcTemplate().execute(sql.toString());
        }
    }

    public List<Map<String, Object>> allGridTable() {
        return getJdbcTemplate().queryForList("SELECT TABLE_NAME AS `name`, TABLE_COMMENT AS `comment` FROM information_schema.tables WHERE table_name LIKE '" + TABLE_PREFIX + "%'");
    }

}
