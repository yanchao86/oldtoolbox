/*
 * Copyright (c) 2010-2014 www.pixshow.net All Rights Reserved
 *
 * File:CustomConfigDao.java Project: ToolboxManager
 * 
 * Creator:4399-lvtu-8 
 * Date:2014年3月7日 上午4:15:37
 * 
 */
package com.pixshow.custom.config;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
public class CustomConfigDao extends BaseDao {
    private static final String tDef = "tb_custom_cfg_def";
    private static final String tVal = "tb_custom_cfg_val";

    public void saveDef(String code, String name, String definition) {
        insert("insert into " + tDef + " (code,name,definition,updateDate,createDate) values (?, ?, ?, ?, ?)", code, name, definition, new Date(), new Date());
    }

    public void updateDef(String code, String name, String definition) {
        update("update " + tDef + " set name=?, definition=?, updateDate=? where code=?", name, definition, new Date(), code);
    }

    public Map<String, Object> findDefByCode(String code) {
        List<Map<String, Object>> list = getJdbcTemplate().queryForList("select * from " + tDef + " where code=?", code);
        return list.size() > 0 ? list.get(0) : null;
    }

    public List<Map<String, Object>> defList() {
        return getJdbcTemplate().queryForList("select * from " + tDef + " order by createDate desc");
    }

    /////////////////////////////////////////////////////////////////////////////////

    public void saveVal(String code, String value) {
        insert("insert into " + tVal + " (code,value) values (?, ?)", code, value);
    }

    public void updateVal(String code, String value) {
        update("update " + tVal + " set value=?, updateDate=? where code=?", value, new Date(), code);
    }

    public Map<String, Object> findValsByDefCode(String defCode) {
        List<Map<String, Object>> list = getJdbcTemplate().queryForList("select * from " + tVal + " where code=?", defCode);
        return list.size() > 0 ? list.get(0) : null;
    }

    /////////////////////////////////////////////////////////////////////////////////
}
