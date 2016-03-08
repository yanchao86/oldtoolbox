package com.pixshow.toolboxmgr.service;

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

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public GameRecommendBean findUse() {
        return gameRecommendDao.find("useType=?", GameSuspend.useType.use);
    }

}
