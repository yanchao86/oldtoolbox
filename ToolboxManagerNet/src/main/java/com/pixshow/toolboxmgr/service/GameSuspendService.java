package com.pixshow.toolboxmgr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.constant.GameSuspend;
import com.pixshow.framework.support.BaseService;
import com.pixshow.toolboxmgr.bean.GameSuspendBean;
import com.pixshow.toolboxmgr.dao.GameSuspendDao;

@Service
public class GameSuspendService extends BaseService {
    @Autowired
    private GameSuspendDao gameSuspendDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int save(GameSuspendBean bean) {
        return gameSuspendDao.save(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(GameSuspendBean bean) {
        gameSuspendDao.update(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public String updateUseType(int id) {
        String result = "";
        GameSuspendBean bean = gameSuspendDao.find("id=?", id);
        //当前状态是未启用，要变成启用
        if (bean.getUseType() == GameSuspend.useType.unUse) {
            //只能有一个开启
            gameSuspendDao.updateUseType(GameSuspend.useType.unUse);
            gameSuspendDao.updateUseTypeById(id, GameSuspend.useType.use);
            result = "已开启！";
        } else {//当前状态是启用，要变成未启用
            gameSuspendDao.updateUseTypeById(id, GameSuspend.useType.unUse);
            result = "已关闭！";
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<GameSuspendBean> findsAll() {
        return gameSuspendDao.finds("");
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<GameSuspendBean> findsRecycle() {
        return gameSuspendDao.findsRecycle("");
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public GameSuspendBean findById(int id) {
        return gameSuspendDao.find("id=?", id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(int id) {
        gameSuspendDao.delete(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateContent(int id, String content) {
        gameSuspendDao.updateContent(id, content);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void restore(int id) {
        gameSuspendDao.restore(id);
    }
}
