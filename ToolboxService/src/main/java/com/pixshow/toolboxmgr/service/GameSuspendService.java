package com.pixshow.toolboxmgr.service;

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

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public GameSuspendBean findUse() {
        return gameSuspendDao.find("useType=?", GameSuspend.useType.use);
    }

}
