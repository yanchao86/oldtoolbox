package com.pixshow.toolboxmgr.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.toolboxmgr.bean.StatWarningBean;

@Repository
public class StatWarningDao extends BaseDao {

    public int save(StatWarningBean bean) {
        return insertBean("tb_toolbox_stat_warning", bean).intValue();
    }

    public void update(StatWarningBean bean) {
        updateBean("tb_toolbox_stat_warning", bean, "id="+bean.getId());
    }

    public void delete(String code) {
        update("delete from tb_toolbox_stat_warning where code=?", code);
    }

    public StatWarningBean find(String where, Object... args) {
        return finds(where, args).size() > 0 ? finds(where, args).get(0) : null;
    }

    public List<StatWarningBean> finds(String where, Object... args) {
        return queryForList("select * from tb_toolbox_stat_warning " + where, StatWarningBean.class, args);
    }
}
