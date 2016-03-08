package com.pixshow.toolboxmgr.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pixshow.constant.GameGlitter;
import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.bean.GameOutGlitterBean;

@Repository
public class GameOutGlitterDao extends BaseDao {

    public int save(GameOutGlitterBean bean) {
        return insertBean("tb_game_out_glitter", bean).intValue();
    }

    public void update(GameOutGlitterBean bean) {
        updateBean("tb_game_out_glitter", bean, "id=" + bean.getId());
    }

    public void updateUseTypeByGitterType(int useType, int glitterType) {
        update("update tb_game_out_glitter set useType=? where glitterType=?", useType, glitterType);
    }
    public void updateUseTypeById(int id, int useType) {
        update("update tb_game_out_glitter set useType=? where id=?", useType, id);
    }

    public void delete(int id) {
        update("INSERT INTO tb_game_out_glitter_recycle SELECT * FROM tb_game_out_glitter AS gg WHERE gg.id=?", id);
        update("delete from tb_game_out_glitter where id=?", id);
        update("update tb_game_out_glitter_recycle set useType=? where id=?", GameGlitter.useType.unUse, id);
    }

    public void restore(int id) {
        update("INSERT INTO tb_game_out_glitter SELECT * FROM tb_game_out_glitter_recycle AS gg WHERE gg.id=?", id);
        update("delete from tb_game_out_glitter_recycle where id=?", id);
    }

    public List<GameOutGlitterBean> finds(String sql, Object... args) {
        sql = StringUtility.isEmpty(sql) ? "1=1" : sql;
        return queryForList("select * from tb_game_out_glitter where " + sql + " order by indexNum desc", GameOutGlitterBean.class, args);
    }

    public GameOutGlitterBean find(String sql, Object... args) {
        if (StringUtility.isEmpty(sql)) { return null; }
        return queryForBean("select * from tb_game_out_glitter where " + sql + " limit 1", GameOutGlitterBean.class, args);
    }

    public List<GameOutGlitterBean> findsRecycle(String sql, Object... args) {
        sql = StringUtility.isEmpty(sql) ? "1=1" : sql;
        return queryForList("select * from tb_game_out_glitter_recycle where " + sql + " order by indexNum desc", GameOutGlitterBean.class, args);
    }

}
