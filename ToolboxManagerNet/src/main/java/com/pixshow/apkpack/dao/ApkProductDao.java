package com.pixshow.apkpack.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pixshow.apkpack.bean.ApkProductBean;
import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.StringUtility;

@Repository
public class ApkProductDao extends BaseDao {

    public int save(ApkProductBean bean) {
        return insertBean("tb_apk_product", bean).intValue();
    }

    public void update(ApkProductBean bean) {
        updateBean("tb_apk_product", bean, "id=" + bean.getId());
    }

    public void delete(String sql, Object... args) {
        if (StringUtility.isEmpty(sql)) { return; }
        update("delete from tb_apk_product where " + sql, args);
    }

    public ApkProductBean find(String sql, Object... args) {
        sql = StringUtility.isEmpty(sql) ? "1=1" : sql;
        return queryForBean("select * from tb_apk_product where " + sql + " limit 1", ApkProductBean.class, args);
    }

    public List<ApkProductBean> finds(String sql, Object... args) {
        sql = StringUtility.isEmpty(sql) ? "1=1" : sql;
        return queryForList("select * from tb_apk_product where " + sql + " order by id desc", ApkProductBean.class, args);
    }

}
