package com.pixshow.toolboxmgr.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.bean.GameOutGlitterBean;

@Repository
public class GameOutGlitterDao extends BaseDao {

    public List<GameOutGlitterBean> finds(String sql, Object... args) {
        sql = StringUtility.isEmpty(sql) ? "1=1" : sql;
        return queryForList("select * from tb_game_out_glitter where " + sql + " order by indexNum desc", GameOutGlitterBean.class, args);
    }

}
