package com.pixshow.apkpack.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pixshow.apkpack.bean.ApkChannelBean;
import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.StringUtility;

@Repository
public class ApkChannelDao extends BaseDao {

    public int save(ApkChannelBean bean) {
        return insertBean("tb_apk_channel", bean).intValue();
    }

    public void update(ApkChannelBean bean) {
        updateBean("tb_apk_channel", bean, "id=" + bean.getId());
    }

    public void delete(String sql, Object... args) {
        if (StringUtility.isEmpty(sql)) { return; }
        update("delete from tb_apk_channel where " + sql, args);
    }

    public ApkChannelBean find(String sql, Object... args) {
        sql = StringUtility.isEmpty(sql) ? "1=1" : sql;
        return queryForBean("select * from tb_apk_channel where " + sql + " limit 1", ApkChannelBean.class, args);
    }

    public List<ApkChannelBean> finds(String sql, Object... args) {
        sql = StringUtility.isEmpty(sql) ? "1=1" : sql;
        return queryForList("select * from tb_apk_channel where " + sql + " order by id desc", ApkChannelBean.class, args);
    }

}
