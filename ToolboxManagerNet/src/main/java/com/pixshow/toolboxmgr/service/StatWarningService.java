package com.pixshow.toolboxmgr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.framework.support.BaseService;
import com.pixshow.toolboxmgr.bean.StatWarningBean;
import com.pixshow.toolboxmgr.dao.StatWarningDao;

@Service
public class StatWarningService extends BaseService {
    @Autowired
    private StatWarningDao statWarningDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int save(StatWarningBean bean) {
        return statWarningDao.save(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(StatWarningBean bean) {
        statWarningDao.update(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(String code) {
        statWarningDao.delete(code);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public StatWarningBean findByCode(String code) {
        return statWarningDao.find("where code=?", code);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<StatWarningBean> findAll() {
        return statWarningDao.finds("");
    }
}
