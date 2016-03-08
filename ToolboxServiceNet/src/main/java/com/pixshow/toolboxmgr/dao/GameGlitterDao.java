package com.pixshow.toolboxmgr.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.bean.GameGlitterBean;

@Repository
public class GameGlitterDao extends BaseDao {

    public List<GameGlitterBean> finds(String sql, Object... args) {
        sql = StringUtility.isEmpty(sql) ? "1=1" : sql;
        return queryForList("select * from tb_game_glitter where " + sql + " order by useType desc, id desc", GameGlitterBean.class, args);
    }

    public GameGlitterBean find(String sql, Object... args) {
        if (StringUtility.isEmpty(sql)) { return null; }
        return queryForBean("select * from tb_game_glitter where " + sql + " limit 1", GameGlitterBean.class, args);
    }

}
