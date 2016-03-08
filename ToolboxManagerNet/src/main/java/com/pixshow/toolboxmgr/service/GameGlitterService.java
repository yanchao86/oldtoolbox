package com.pixshow.toolboxmgr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.constant.GameGlitter;
import com.pixshow.framework.support.BaseService;
import com.pixshow.toolboxmgr.bean.GameGlitterBean;
import com.pixshow.toolboxmgr.dao.GameGlitterDao;

@Service
public class GameGlitterService extends BaseService {

    @Autowired
    private GameGlitterDao gameGlitterDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int save(GameGlitterBean bean) {
        return gameGlitterDao.save(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(GameGlitterBean bean) {
        gameGlitterDao.update(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public String updateUseType(int id) {
        String result = "";
        GameGlitterBean bean = gameGlitterDao.find("id=?", id);
        //默认的闪屏
        if (bean.getGlitterType() == GameGlitter.glitterType.isDefault) {
            //当前状态是未启用，要变成启用
            if (bean.getUseType() == GameGlitter.useType.unUse) {
                //默认闪屏只能有一个开启
                gameGlitterDao.updateUseTypeByGitterType(GameGlitter.useType.unUse, GameGlitter.glitterType.isDefault);
                gameGlitterDao.updateUseTypeById(id, GameGlitter.useType.use);
                result = "当前默认闪屏已开启！";
            } else {//当前状态是启用，要变成未启用
                gameGlitterDao.updateUseTypeById(id, GameGlitter.useType.unUse);
                result = "当前默认闪屏已关闭！";
            }
            //节日闪屏
        } else {
            gameGlitterDao.updateUseTypeById(id, bean.getUseType() == GameGlitter.useType.use ? GameGlitter.useType.unUse : GameGlitter.useType.use);
            result = bean.getUseType() == GameGlitter.useType.unUse ? "当前节日闪屏已开启！" : "当前节日闪屏已关闭！";
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(int id) {
        gameGlitterDao.delete(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void restore(int id) {
        gameGlitterDao.restore(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<GameGlitterBean> findsAll() {
        return gameGlitterDao.finds("");
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<GameGlitterBean> findsRecycle() {
        return gameGlitterDao.findsRecycle("");
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public GameGlitterBean find(String sql, Object... args) {
        return gameGlitterDao.find(sql, args);
    }
}
