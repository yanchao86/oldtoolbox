package com.pixshow.toolboxmgr.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.bean.ApkUploadBean;

@Repository
public class ApkUploadDao extends BaseDao {

    public int save(ApkUploadBean bean) {
        return insertBean("tb_apk_upload", bean).intValue();
    }

    public void update(ApkUploadBean bean) {
        updateBean("tb_apk_upload", bean, "id=" + bean.getId());
    }

    public List<ApkUploadBean> finds(String sql, Object... args) {
        sql = StringUtility.isEmpty(sql) ? "1=1" : sql;
        return queryForList("select * from tb_apk_upload where " + sql, ApkUploadBean.class, args);
    }
    
    public List<ApkUploadBean> findsSha1() {
        return queryForList("select * from (" + //
                " SELECT * FROM tb_apk_upload" + //
                " where sha1 is not null and sha1!='' order by createDate desc" + //
                " ) as a" + //
                " group by a.fileName ", ApkUploadBean.class);
    }

    public ApkUploadBean find(String sql, Object... args) {
        sql = StringUtility.isEmpty(sql) ? "1=1" : sql;
        return queryForBean("select * from tb_apk_upload where " + sql + " limit 1", ApkUploadBean.class, args);
    }
}
