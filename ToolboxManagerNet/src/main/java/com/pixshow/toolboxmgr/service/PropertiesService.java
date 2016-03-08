package com.pixshow.toolboxmgr.service;

import java.util.List;
import java.util.Map;

import org.jf.smali.smaliParser.integer_literal_return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.toolboxmgr.dao.PropertiesDao;

@Service
public class PropertiesService {

    @Autowired
    private PropertiesDao propertyDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> getIBoxOpen(int key) {
        return propertyDao.getIBoxOpen(key);
    }
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> getNotificationPV(int key) {
    	return propertyDao.getNotificationPV(key);
    }
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> getNotificationUV(int key) {
    	return propertyDao.getNotificationUV(key);
    }
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> getAppsPV(int key) {
    	return propertyDao.getAppsPV(key);
    }
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> getAppsUV(int key) {
    	return propertyDao.getAppsUV(key);
    }
    
    
    
    
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public String getValue(String key) {
        return propertyDao.getValue(key);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> likeValue(String like) {
        return propertyDao.likeValue(like);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public String getValue(String key, String defaultValue) {
        String value = propertyDao.getValue(key);
        return value == null ? defaultValue : value;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void setValue(String key, String value) {
        propertyDao.setValue(key, value);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> getValues(String key) {
        return propertyDao.getValues(key);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateValue(String key, String value) {
        propertyDao.updateValue(key, value);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delchannel(String key) {
        propertyDao.delchannel(key);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<String> getValue(String... keys) {
        try {
            return propertyDao.getValue(keys);
        } catch (Exception e) {
            return null;
        }
    }
}
