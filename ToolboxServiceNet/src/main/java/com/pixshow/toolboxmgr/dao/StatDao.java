package com.pixshow.toolboxmgr.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;

@Repository
public class StatDao extends BaseDao {

    public void countDayStat(String code, int day, int count) {
        insert("INSERT INTO tb_toolbox_day_stat(code,day,count) VALUES (?,?,1) ON DUPLICATE KEY UPDATE count=count+" + count, code, day);
    }

    public void countDayStat(String code, String productCode, String productVersion, String sdkVersion, int day, int count) {
        insert("INSERT INTO tb_toolbox_day_stat_new(code,productCode,productVersion,sdkVersion,day,count) VALUES (?,?,?,?,?,1) ON DUPLICATE KEY UPDATE count=count+" + count, code, productCode, productVersion, sdkVersion, day);
    }

    public boolean findDayStatUv(String uid, String code, int day) {
        List<Map<String, Object>> list = getJdbcTemplate().queryForList("select * from tb_toolbox_day_stat_uv where uid=? and code=? and day=?", uid, code, day);
        if (list.size() > 0) {
            return false;
        } else {
            insert("insert into tb_toolbox_day_stat_uv(uid,code,day) values (?,?,?)", uid, code, day);
            return true;
        }
    }

    public boolean findDayStatUv(String uid, String code, String productCode, String productVersion, String sdkVersion, int day) {
        List<Map<String, Object>> list = getJdbcTemplate().queryForList("select * from tb_toolbox_day_stat_uv_new where uid=? and code=? and productCode=? and productVersion=? and sdkVersion=? and day=?", uid, code, productCode, productVersion,
                sdkVersion, day);
        if (list.size() > 0) {
            return false;
        } else {
            insert("insert into tb_toolbox_day_stat_uv_new(uid,code,productCode,productVersion,sdkVersion,day) values (?,?,?,?,?,?)", uid, code, productCode, productVersion, sdkVersion, day);
            return true;
        }
    }

}
