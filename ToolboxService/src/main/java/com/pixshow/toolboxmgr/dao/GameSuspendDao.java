package com.pixshow.toolboxmgr.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.bean.GameSuspendBean;

@Repository
public class GameSuspendDao extends BaseDao {

    public List<GameSuspendBean> finds(String sql, Object... args) {
        sql = StringUtility.isEmpty(sql) ? "1=1" : sql;
        return queryForList("select * from tb_game_suspend where " + sql + " order by useType desc, id desc", GameSuspendBean.class, args);
    }

    public GameSuspendBean find(String sql, Object... args) {
        if (StringUtility.isEmpty(sql)) { return null; }
        return queryForBean("select * from tb_game_suspend where " + sql + " limit 1", GameSuspendBean.class, args);
    }

}
