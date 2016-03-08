package com.pixshow.apkpack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.apkpack.bean.ApkSignBean;
import com.pixshow.apkpack.dao.ApkSignDao;
import com.pixshow.apkpack.utils.ApkSignWorker;
import com.pixshow.apkpack.utils.ApkSignWorker.JobInfo;
import com.pixshow.framework.support.BaseService;

@Service
public class ApkSignService extends BaseService implements ApkSignWorker.HandlerCallback {
    @Autowired
    private ApkSignDao apkSignDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int save(ApkSignBean bean) {
        return apkSignDao.save(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(ApkSignBean bean) {
        apkSignDao.update(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(String sql, Object... args) {
        apkSignDao.delete(sql, args);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ApkSignBean find(String sql, Object... args) {
        return apkSignDao.find(sql, args);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ApkSignBean> finds(String sql, Object... args) {
        return apkSignDao.finds(sql, args);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ApkSignBean> findAll() {
        return apkSignDao.finds("");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void callback(String msg, JobInfo job) {
        apkSignDao.updateMsg(job.id, msg);
        
    }

}
