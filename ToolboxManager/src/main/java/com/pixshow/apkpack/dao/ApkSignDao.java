package com.pixshow.apkpack.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pixshow.apkpack.bean.ApkSignBean;
import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.StringUtility;

@Repository
public class ApkSignDao extends BaseDao {

    public int save(ApkSignBean bean) {
        return insertBean("tb_apk_sign", bean).intValue();
    }

    public void update(ApkSignBean bean) {
        updateBean("tb_apk_sign", bean, "id=" + bean.getId());
    }

    public void updateMsg(int id, String msg) {
        update("update tb_apk_sign set msg=? where id=?", msg, id);
    }

    public void delete(String sql, Object... args) {
        if (StringUtility.isEmpty(sql)) { return; }
        update("delete from tb_apk_sign where " + sql, args);
    }

    public ApkSignBean find(String sql, Object... args) {
        sql = StringUtility.isEmpty(sql) ? "1=1" : sql;
        return queryForBean("select * from tb_apk_sign where " + sql + " limit 1", ApkSignBean.class, args);
    }

    public List<ApkSignBean> finds(String sql, Object... args) {
        sql = StringUtility.isEmpty(sql) ? "1=1" : sql;
        return queryForList("select * from tb_apk_sign where " + sql + " order by id desc", ApkSignBean.class, args);
    }

}
