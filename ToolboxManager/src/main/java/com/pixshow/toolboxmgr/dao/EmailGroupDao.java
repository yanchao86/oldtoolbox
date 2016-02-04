package com.pixshow.toolboxmgr.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.StringUtility;

@Repository
public class EmailGroupDao extends BaseDao {

    public void addEmailGroup(String name) {
        insert("insert into tb_email_group (name,createDate) values (?, ?)", name, new Date());
    }

    public void delEmailGroup(int id) {
        update("delete from tb_email_group where id=?", id);
    }

    public List<Map<String, Object>> findEmailGroup(String sql, Object... arges) {
        return getJdbcTemplate().queryForList("select * from tb_email_group where " + sql, arges);
    }

    ///////////////////////////////////////////////

    public void addEmailInGroup(String email, String name, int groupId) {
        insert("insert into tb_email_group_member (groupId, email, name, createDate) values (?, ?, ?, ?)", groupId, email, name, new Date());
    }

    public void delEmailById(int id) {
        update("delete from tb_email_group_member where id=?", id);
    }

    public void delEmailInGroup(int groupId) {
        update("delete from tb_email_group_member where groupId=?", groupId);
    }

    public List<Map<String, Object>> findEmailInGroup(String sql, Object... arges) {
        sql = StringUtility.isEmpty(sql) ? "1=1" : sql;
        return getJdbcTemplate().queryForList("select * from tb_email_group_member where " + sql, arges);
    }

}
