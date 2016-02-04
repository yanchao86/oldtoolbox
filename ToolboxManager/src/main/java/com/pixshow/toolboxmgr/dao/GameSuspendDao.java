package com.pixshow.toolboxmgr.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pixshow.constant.GameSuspend;
import com.pixshow.framework.support.BaseDao;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.bean.GameSuspendBean;

@Repository
public class GameSuspendDao extends BaseDao {
    public int save(GameSuspendBean bean) {
        return insertBean("tb_game_suspend", bean).intValue();
    }

    public void update(GameSuspendBean bean) {
        updateBean("tb_game_suspend", bean, "id=" + bean.getId());
    }

    public void updateUseType(int useType) {
        update("update tb_game_suspend set useType=?", useType);
    }

    public void updateUseTypeById(int id, int useType) {
        update("update tb_game_suspend set useType=? where id=?", useType, id);
    }

    public void updateContent(int id, String content) {
        update("update tb_game_suspend set content=? where id=?", content, id);
    }

    public List<GameSuspendBean> finds(String sql, Object... args) {
        sql = StringUtility.isEmpty(sql) ? "1=1" : sql;
        return queryForList("select * from tb_game_suspend where " + sql + " order by useType desc, id desc", GameSuspendBean.class, args);
    }

    public List<GameSuspendBean> findsRecycle(String sql, Object... args) {
        sql = StringUtility.isEmpty(sql) ? "1=1" : sql;
        return queryForList("select * from tb_game_suspend_recycle where " + sql + " order by useType desc, id desc", GameSuspendBean.class, args);
    }

    public GameSuspendBean find(String sql, Object... args) {
        if (StringUtility.isEmpty(sql)) { return null; }
        return queryForBean("select * from tb_game_suspend where " + sql + " limit 1", GameSuspendBean.class, args);
    }

    public void delete(int id) {
        update("INSERT INTO tb_game_suspend_recycle SELECT * FROM tb_game_suspend AS gg WHERE gg.id=?", id);
        update("delete from tb_game_suspend where id=?", id);
        update("update tb_game_suspend_recycle set useType=? where id=?", GameSuspend.useType.unUse, id);
    }

    public void restore(int id) {
        update("INSERT INTO tb_game_suspend SELECT * FROM tb_game_suspend_recycle AS gg WHERE gg.id=?", id);
        update("delete from tb_game_suspend_recycle where id=?", id);
    }

}
