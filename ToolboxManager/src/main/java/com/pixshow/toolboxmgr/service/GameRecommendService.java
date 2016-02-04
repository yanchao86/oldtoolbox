package com.pixshow.toolboxmgr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.constant.GameSuspend;
import com.pixshow.framework.support.BaseService;
import com.pixshow.toolboxmgr.bean.GameRecommendBean;
import com.pixshow.toolboxmgr.dao.GameRecommendDao;

@Service
public class GameRecommendService extends BaseService {
    @Autowired
    private GameRecommendDao gameRecommendDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int save(GameRecommendBean bean) {
        return gameRecommendDao.save(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(GameRecommendBean bean) {
        gameRecommendDao.update(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public String updateUseType(int id) {
        String result = "";
        GameRecommendBean bean = gameRecommendDao.find("id=?", id);
        //当前状态是未启用，要变成启用
        if (bean.getUseType() == GameSuspend.useType.unUse) {
            //只能有一个开启
            gameRecommendDao.updateUseType(GameSuspend.useType.unUse);
            gameRecommendDao.updateUseTypeById(id, GameSuspend.useType.use);
            result = "已开启！";
        } else {//当前状态是启用，要变成未启用
            gameRecommendDao.updateUseTypeById(id, GameSuspend.useType.unUse);
            result = "已关闭！";
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<GameRecommendBean> findsAll() {
        return gameRecommendDao.finds("");
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public GameRecommendBean findById(int id) {
        return gameRecommendDao.find("id=?", id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(int id) {
        gameRecommendDao.delete(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateContent(int id, String content) {
        gameRecommendDao.updateContent(id, content);
    }

}
