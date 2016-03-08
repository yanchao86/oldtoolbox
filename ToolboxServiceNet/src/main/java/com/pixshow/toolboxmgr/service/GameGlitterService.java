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

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<GameGlitterBean> findsUse() {
        return gameGlitterDao.finds("useType=?", GameGlitter.useType.use);
    }

}
