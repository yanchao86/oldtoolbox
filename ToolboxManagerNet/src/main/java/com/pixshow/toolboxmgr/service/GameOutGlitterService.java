package com.pixshow.toolboxmgr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.constant.GameGlitter;
import com.pixshow.framework.support.BaseService;
import com.pixshow.toolboxmgr.bean.GameOutGlitterBean;
import com.pixshow.toolboxmgr.dao.GameOutGlitterDao;

@Service
public class GameOutGlitterService extends BaseService {

    @Autowired
    private GameOutGlitterDao gameOutGlitterDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int save(GameOutGlitterBean bean) {
        return gameOutGlitterDao.save(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(GameOutGlitterBean bean) {
        gameOutGlitterDao.update(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public String updateUseType(int id) {
        String result = "";
        GameOutGlitterBean bean = gameOutGlitterDao.find("id=?", id);
        gameOutGlitterDao.updateUseTypeById(id, bean.getUseType() == GameGlitter.useType.use ? GameGlitter.useType.unUse : GameGlitter.useType.use);
        result = bean.getUseType() == GameGlitter.useType.unUse ? "退出闪屏已开启！" : "退出闪屏已关闭！";
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(int id) {
        gameOutGlitterDao.delete(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void restore(int id) {
        gameOutGlitterDao.restore(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<GameOutGlitterBean> findsAll() {
        return gameOutGlitterDao.finds("");
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<GameOutGlitterBean> findsRecycle() {
        return gameOutGlitterDao.findsRecycle("");
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public GameOutGlitterBean findById(int id) {
        return gameOutGlitterDao.find("id=?", id);
    }
}
