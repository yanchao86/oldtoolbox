package com.pixshow.toolboxmgr.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.bean.GameRecommendBean;

@Repository
public class GameRecommendDao extends BaseDao {

    public int save(GameRecommendBean bean) {
        return insertBean("tb_game_recommend", bean).intValue();
    }

    public void update(GameRecommendBean bean) {
        updateBean("tb_game_recommend", bean, "id=" + bean.getId());
    }

    public void updateContent(int id, String content) {
        update("update tb_game_recommend set content=? where id=?", content, id);
    }

    public void updateUseType(int useType) {
        update("update tb_game_recommend set useType=?", useType);
    }

    public void updateUseTypeById(int id, int useType) {
        update("update tb_game_recommend set useType=? where id=?", useType, id);
    }

    public List<GameRecommendBean> finds(String sql, Object... args) {
        sql = StringUtility.isEmpty(sql) ? "1=1" : sql;
        return queryForList("select * from tb_game_recommend where " + sql + " order by useType desc, id desc", GameRecommendBean.class, args);
    }

    public GameRecommendBean find(String sql, Object... args) {
        if (StringUtility.isEmpty(sql)) { return null; }
        return queryForBean("select * from tb_game_recommend where " + sql + " limit 1", GameRecommendBean.class, args);
    }

    public void delete(int id) {
        update("delete from tb_game_recommend where id=?", id);
    }

}
