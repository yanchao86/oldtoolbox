package com.pixshow.toolboxmgr.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.framework.support.BaseService;
import com.pixshow.toolboxmgr.dao.EmailGroupDao;

@Service
public class EmailGroupService extends BaseService {

    @Autowired
    private EmailGroupDao emailGroupDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addEmailGroup(String name) {
        emailGroupDao.addEmailGroup(name);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delEmailGroup(int id) {
        emailGroupDao.delEmailGroup(id);
        emailGroupDao.delEmailInGroup(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> findEmailGroup() {
        return emailGroupDao.findEmailGroup("1=?", 1);
    }

    ///////////////////////////////////////////////
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addEmailInGroup(String email, String name, int groupId) {
        emailGroupDao.addEmailInGroup(email, name, groupId);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delEmailById(int id) {
        emailGroupDao.delEmailById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delEmailInGroup(int groupId) {
        emailGroupDao.delEmailInGroup(groupId);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> findEmailInGroup(int groupId) {
        return emailGroupDao.findEmailInGroup("groupId=?", groupId);
    }

}
