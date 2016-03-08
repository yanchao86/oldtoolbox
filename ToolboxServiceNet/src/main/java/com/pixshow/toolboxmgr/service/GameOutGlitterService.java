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

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<GameOutGlitterBean> findsUse() {
        return gameOutGlitterDao.finds("useType=?", GameGlitter.useType.use);
    }
}
