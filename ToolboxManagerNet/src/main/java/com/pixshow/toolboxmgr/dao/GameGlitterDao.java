package com.pixshow.toolboxmgr.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pixshow.constant.GameGlitter;
import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.bean.GameGlitterBean;

@Repository
public class GameGlitterDao extends BaseDao {

    public int save(GameGlitterBean bean) {
        return insertBean("tb_game_glitter", bean).intValue();
    }

    public void update(GameGlitterBean bean) {
        updateBean("tb_game_glitter", bean, "id=" + bean.getId());
    }

    public void updateUseTypeByGitterType(int useType, int glitterType) {
        update("update tb_game_glitter set useType=? where glitterType=?", useType, glitterType);
    }
    public void updateUseTypeById(int id, int useType) {
        update("update tb_game_glitter set useType=? where id=?", useType, id);
    }

    public void delete(int id) {
        update("INSERT INTO tb_game_glitter_recycle SELECT * FROM tb_game_glitter AS gg WHERE gg.id=?", id);
        update("delete from tb_game_glitter where id=?", id);
        update("update tb_game_glitter_recycle set useType=? where id=?", GameGlitter.useType.unUse, id);
    }

    public void restore(int id) {
        update("INSERT INTO tb_game_glitter SELECT * FROM tb_game_glitter_recycle AS gg WHERE gg.id=?", id);
        update("delete from tb_game_glitter_recycle where id=?", id);
    }

    public List<GameGlitterBean> finds(String sql, Object... args) {
        sql = StringUtility.isEmpty(sql) ? "1=1" : sql;
        return queryForList("select * from tb_game_glitter where " + sql + " order by useType desc, id desc", GameGlitterBean.class, args);
    }

    public GameGlitterBean find(String sql, Object... args) {
        if (StringUtility.isEmpty(sql)) { return null; }
        return queryForBean("select * from tb_game_glitter where " + sql + " limit 1", GameGlitterBean.class, args);
    }

    public List<GameGlitterBean> findsRecycle(String sql, Object... args) {
        sql = StringUtility.isEmpty(sql) ? "1=1" : sql;
        return queryForList("select * from tb_game_glitter_recycle where " + sql + " order by useType desc, id desc", GameGlitterBean.class, args);
    }

}
