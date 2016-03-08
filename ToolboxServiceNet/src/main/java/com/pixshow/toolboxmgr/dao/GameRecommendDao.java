package com.pixshow.toolboxmgr.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.bean.GameRecommendBean;

@Repository
public class GameRecommendDao extends BaseDao {

    public List<GameRecommendBean> finds(String sql, Object... args) {
        sql = StringUtility.isEmpty(sql) ? "1=1" : sql;
        return queryForList("select * from tb_game_recommend where " + sql + " order by useType desc, id desc", GameRecommendBean.class, args);
    }

    public GameRecommendBean find(String sql, Object... args) {
        if (StringUtility.isEmpty(sql)) { return null; }
        return queryForBean("select * from tb_game_recommend where " + sql + " limit 1", GameRecommendBean.class, args);
    }

}
