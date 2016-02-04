package com.pixshow.toolboxmgr.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.DateUtility;
import com.pixshow.framework.utils.SqlUtility;
import com.pixshow.framework.utils.StringUtility;

@Repository
public class StatCodeDao extends BaseDao {

    public List<Map<String, Object>> statByCode(String code) {
        return getJdbcTemplate().queryForList("select s.*, c.name from tb_toolbox_day_stat s" + //
                " join tb_toolbox_stat_cat_code c on (s.code = c.code)" + //
                " where s.code like ? order by day desc", code);
    }

    public List<Map<String, Object>> statByLikeCode(String code) {
        return getJdbcTemplate().queryForList("select s.*, c.name from tb_toolbox_day_stat s" + //
                " join tb_toolbox_stat_cat_code c on (s.code = c.code)" + //
                " where s.code like ? order by day desc", "%" + code + "%");
    }

    public List<Map<String, Object>> statWeekByCode(List<Object> codes) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -7);
        int day = Integer.parseInt(DateUtility.format(c, "yyyyMMdd"));
        List<Object> codes_ = new ArrayList<Object>();
        codes_.addAll(codes);
        codes_.add(day);
        return getJdbcTemplate().queryForList("select * from tb_toolbox_day_stat where " + SqlUtility.in("code", codes_.size() - 1) + " and day>=?", codes_.toArray());
    }

    public List<Map<String, Object>> statCountByCode(List<Object> codes) {
        return getJdbcTemplate().queryForList("select code, sum(count) as count from tb_toolbox_day_stat where " + SqlUtility.in("code", codes.size()) + " group by code", codes.toArray());
    }

    public List<Map<String, Object>> findAllDayStatCodes() {
        return getJdbcTemplate().queryForList("select code from tb_toolbox_day_stat where code NOT IN(select code from tb_toolbox_stat_cat_code) GROUP BY code");
    }

    public void insertCat(String name) {
        insert("insert into tb_toolbox_stat_cat (name,createDate) values (?,?)", name, new Date());
    }

    public void insertCatCode(int catId, String name, String code) {
        insert("insert into tb_toolbox_stat_cat_code (catId,name,code,careteDate) values (?,?,?,?)", catId, name, code, new Date());
    }

    public void deleteCat(int catId) {
        update("delete from tb_toolbox_stat_cat where id=?", catId);
        update("delete from tb_toolbox_stat_cat_code where catId=?", catId);
    }

    public void updateCatCodeName(int codeId, String name) {
        update("update tb_toolbox_stat_cat_code set name=? where id=?", name, codeId);
    }

    public void deleteCatCode(int codeId) {
        update("delete from tb_toolbox_stat_cat_code where id=?", codeId);
    }

    public void deleteCatCodes(String statCatCodeIds) {
        update("delete from tb_toolbox_stat_cat_code where id in (" + statCatCodeIds + ")");
    }

    public List<Map<String, Object>> findCats(String sql, Object... args) {
        return getJdbcTemplate().queryForList("select * from tb_toolbox_stat_cat " + sql, args);
    }

    public List<Map<String, Object>> findCatCodes(String sql, Object... args) {
        return getJdbcTemplate().queryForList("select * from tb_toolbox_stat_cat_code " + sql, args);
    }

    public List<Map<String, Object>> findCodesNotInCatIdAndLikeByCode(int catId, String code) {
        return getJdbcTemplate().queryForList("select code from tb_toolbox_day_stat where code not in (select code from tb_toolbox_stat_cat_code where catId=?) and code like ? group by code", catId, code);
    }

    //------keyValue-----------------
    public List<Map<String, Object>> findKeyValue(String sql, Object... args) {
        return getJdbcTemplate().queryForList("select * from tb_toolbox_stat_key_value " + sql, args);
    }

    public void insertKeyValue(String name, String value) {
        insert("insert into tb_toolbox_stat_key_value (name,value) values (?,?)", name, value);
    }

    public void updateKeyValue(int id, String value) {
        update("update tb_toolbox_stat_key_value set value=? where id=?", value, id);
    }

    public void deleteKeyValue(int id) {
        update("delete from tb_toolbox_stat_key_value where id=?", id);
    }

    //------keyValue-----------------

    //------ 统计  ------------------
    public List<Map<String, Object>> statQuery(String sql, Object... param) {
        return getJdbcTemplate().queryForList("select * from tb_toolbox_day_stat " + sql, param);
    }

    public List<Map<String, Object>> statByCodeAndDay(String prefixCode, String suffixCode, int startDay, int endDay) {
        StringBuilder sql = new StringBuilder();
        List<Object> args = new ArrayList<Object>();
        sql.append("SELECT `day`, SUM(count) AS count FROM tb_toolbox_day_stat ");
        sql.append("WHERE `day` BETWEEN ? AND ? ");
        args.add(startDay);
        args.add(endDay);
        if (StringUtility.isNotEmpty(prefixCode)) {
            sql.append("AND `code` LIKE ? ");
            args.add(prefixCode + "%");
        }
        if (StringUtility.isNotEmpty(suffixCode)) {
            sql.append("AND `code` LIKE ? ");
            args.add("%" + suffixCode);
        }
        sql.append("GROUP BY `day` ");
        sql.append("ORDER BY `day` ");

        return getJdbcTemplate().queryForList(sql.toString(), args.toArray());
    }
}
