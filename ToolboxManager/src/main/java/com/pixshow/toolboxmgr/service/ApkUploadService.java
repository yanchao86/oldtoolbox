package com.pixshow.toolboxmgr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.framework.support.BaseService;
import com.pixshow.toolboxmgr.bean.ApkUploadBean;
import com.pixshow.toolboxmgr.dao.ApkUploadDao;

@Service
public class ApkUploadService extends BaseService {

    @Autowired
    private ApkUploadDao apkUploadDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int save(ApkUploadBean bean) {
        return apkUploadDao.save(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(ApkUploadBean bean) {
        apkUploadDao.update(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ApkUploadBean find(String fileName, String uploadServer, String uploadFolder) {
        return apkUploadDao.find("fileName=? and uploadServer=? and uploadFolder=?", fileName, uploadServer, uploadFolder);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ApkUploadBean> findsSha1() {
        return apkUploadDao.findsSha1();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ApkUploadBean> findAll() {
        return apkUploadDao.finds("1=1 order by createDate desc");
    }

}
