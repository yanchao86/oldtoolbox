package com.pixshow.apkpack.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pixshow.apkpack.bean.ApkKeyBean;
import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.StringUtility;

@Repository
public class ApkKeyDao extends BaseDao {

    public int save(ApkKeyBean bean) {
        return insertBean("tb_apk_key", bean).intValue();
    }

    public void update(ApkKeyBean bean) {
        updateBean("tb_apk_key", bean, "id=" + bean.getId());
    }

    public void delete(String sql, Object... args) {
        if (StringUtility.isEmpty(sql)) { return; }
        update("delete from tb_apk_key where " + sql, args);
    }

    public ApkKeyBean find(String sql, Object... args) {
        sql = StringUtility.isEmpty(sql) ? "1=1" : sql;
        return queryForBean("select * from tb_apk_key where " + sql + " limit 1", ApkKeyBean.class, args);
    }

    public List<ApkKeyBean> finds(String sql, Object... args) {
        sql = StringUtility.isEmpty(sql) ? "1=1" : sql;
        return queryForList("select * from tb_apk_key where " + sql, ApkKeyBean.class, args);
    }

}
