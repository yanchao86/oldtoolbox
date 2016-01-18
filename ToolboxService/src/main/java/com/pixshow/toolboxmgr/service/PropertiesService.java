package com.pixshow.toolboxmgr.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.dao.PropertiesDao;

@Service
public class PropertiesService {

    @Autowired
    PropertiesDao propertyDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public String getValue(String key) {
        try {
            return propertyDao.getValue(key);
        } catch (Exception e) {
            return "";
        }
    }

    public List<String> getValue(String... keys) {
        try {
            return propertyDao.getValue(keys);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Map<String, Object>> likeValue(String like) {
        return propertyDao.likeValue(like);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public String getValue(String key, String defaultValue) {
        String tmp = "";
        try {
            tmp = propertyDao.getValue(key);
        } catch (Exception e) {
            return defaultValue;
        }
        if (StringUtility.isNotEmpty(tmp)) return tmp;
        return defaultValue;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void setValue(String key, String value) {
        propertyDao.setValue(key, value);
    }
}
