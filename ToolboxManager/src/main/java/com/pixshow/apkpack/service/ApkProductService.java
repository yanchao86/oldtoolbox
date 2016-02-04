package com.pixshow.apkpack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.apkpack.bean.ApkProductBean;
import com.pixshow.apkpack.dao.ApkProductDao;
import com.pixshow.framework.support.BaseService;

@Service
public class ApkProductService extends BaseService {
    @Autowired
    private ApkProductDao apkProductDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int save(ApkProductBean bean) {
        return apkProductDao.save(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(ApkProductBean bean) {
        apkProductDao.update(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(String sql, Object... args) {
        apkProductDao.delete(sql, args);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ApkProductBean findById(int id) {
        return apkProductDao.find("id=?", id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ApkProductBean> findAll() {
        return apkProductDao.finds("");
    }

}
