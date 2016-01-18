package com.pixshow.toolboxmgr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.framework.support.BaseService;
import com.pixshow.toolboxmgr.bean.UserBean;
import com.pixshow.toolboxmgr.dao.UserDao;

@Service
public class UserService extends BaseService {
    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void createUserAppCode(String appCode) {
        userDao.createUserAppCode(appCode);
    }

    /**
     * 
     * @param area '1：国内，2：国外，3：全部'
     * @param appCode 应用code
     * @param appVersion 应用版本code
     * @return
     *
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<UserBean> findByArea(int area, String appCode, String appVersion) {
        return userDao.findByArea(area, appCode, appVersion);
    }

    /**
     * 
     * @param area '1：国内，2：国外，3：全部'
     * @param appCode 应用code
     * @return
     *
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<UserBean> findByArea(int area, String appCode) {
        return userDao.findByArea(area, appCode);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int addUser(UserBean user, String appCode) {
        return userDao.addUser(user, appCode);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateUser(UserBean user, String appCode) {
        userDao.updateUser(user, appCode);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public UserBean findByMac(String mac, String appCode) {
        return userDao.findByMac(mac, appCode);
    }
}
