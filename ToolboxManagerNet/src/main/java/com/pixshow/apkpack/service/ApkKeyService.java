package com.pixshow.apkpack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.apkpack.bean.ApkKeyBean;
import com.pixshow.apkpack.dao.ApkKeyDao;
import com.pixshow.framework.support.BaseService;

@Service
public class ApkKeyService extends BaseService {
    @Autowired
    private ApkKeyDao apkKeyDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int save(ApkKeyBean bean) {
        return apkKeyDao.save(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(ApkKeyBean bean) {
        apkKeyDao.update(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(String sql, Object... args) {
        apkKeyDao.delete(sql, args);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ApkKeyBean findById(int id) {
        return apkKeyDao.find("id=?", id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ApkKeyBean> findAll() {
        return apkKeyDao.finds("");
    }

}
