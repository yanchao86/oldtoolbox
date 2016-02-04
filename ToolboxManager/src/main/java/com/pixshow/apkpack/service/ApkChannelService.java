package com.pixshow.apkpack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.apkpack.bean.ApkChannelBean;
import com.pixshow.apkpack.dao.ApkChannelDao;
import com.pixshow.framework.support.BaseService;
import com.pixshow.framework.utils.SqlUtility;

@Service
public class ApkChannelService extends BaseService {
    @Autowired
    private ApkChannelDao apkChannelDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int save(ApkChannelBean bean) {
        return apkChannelDao.save(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(ApkChannelBean bean) {
        apkChannelDao.update(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(String sql, Object... args) {
        apkChannelDao.delete(sql, args);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ApkChannelBean findById(int id) {
        return apkChannelDao.find("id=?", id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ApkChannelBean findByChannel(String channel) {
        return apkChannelDao.find("channel=?", channel);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ApkChannelBean> findByIds(List<Integer> ids) {
        return apkChannelDao.finds(SqlUtility.in("id", ids.size()), ids.toArray());
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ApkChannelBean> findAll() {
        return apkChannelDao.finds("");
    }

}
