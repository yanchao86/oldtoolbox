package com.pixshow.toolboxmgr.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;

@Repository
public class DownloadDao extends BaseDao {

    /**
     * 通过工具ID 查询下载地址
     */
    public String getUrl(String table, int id) {
        List<Map<String, Object>> list = getJdbcTemplate().queryForList("select downloadUrl from " + table + " where id=?", id);
        return list.size() > 0 ? list.get(0).get("downloadUrl").toString() : "";
    }

    /**
     * 通过工具ID 添加下载次数
     */
    public void addDownloadCount(String table, int id) {
        update("UPDATE " + table + " SET downloadCount=downloadCount+1 WHERE id=?", id);
    }

    ///////////////////////////////////////////////////////////////////////////
    public void addDownloadCountDay(String table, int toolId, int day) {
        insert("insert into " + table + "_dl_day_stat (toolId,day,count) values (?, ?, 1)", toolId, day);
    }

    public void updateDownloadCountDay(String table, int toolId, int day) {
        update("update " + table + "_dl_day_stat SET count=count+1 WHERE toolId=? and day=?", toolId, day);
    }
}
