package com.pixshow.toolboxmgr.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.dao.CustomDao;

@Service
public class CustomService {

    @Autowired
    private CustomDao customDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public String getConfig(String key) {
        return customDao.getConfig(key);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<String> getConfig(String... keys) {
        try {
            return customDao.getConfig(keys);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public String getValue(String key) {
        try {
            return customDao.getValue(key);
        } catch (Exception e) {
            return "";
        }
    }

    public List<String> getValue(String... keys) {
        try {
            return customDao.getValue(keys);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public String getValue(String key, String defaultValue) {
        String tmp = "";
        try {
            tmp = customDao.getValue(key);
        } catch (Exception e) {
            return defaultValue;
        }
        if (StringUtility.isNotEmpty(tmp)) return tmp;
        return defaultValue;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void setValue(String key, String value) {
        customDao.setValue(key, value);
    }

}
