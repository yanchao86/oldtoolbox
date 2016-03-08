/*
 * Copyright (c) 2010-2014 www.pixshow.net All Rights Reserved
 *
 * File:CustomConfigDao.java Project: ToolboxManager
 * 
 * Creator:4399-lvtu-8 
 * Date:2014年3月7日 上午4:15:37
 * 
 */
package com.pixshow.custom.config;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.framework.support.BaseService;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since 2014年3月7日
 */
@Service
public class CustomConfigService extends BaseService {
    @Autowired
    private CustomConfigDao customConfigDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void saveDef(String code, String name, String definition) {
        customConfigDao.saveDef(code, name, definition);
//        saveKey(code, definition);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateDef(String code, String name, String definition) {
        customConfigDao.updateDef(code, name, definition);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map<String, Object> findDefByCode(String code) {
        return customConfigDao.findDefByCode(code);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> defList() {
        return customConfigDao.defList();
    }

    ////////////////////////////////////////////////////////
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void saveKey(String outKey, String definition) {
        JSONObject json = JSONObject.fromObject(definition);
        Iterator<String> it = json.keys();
        while (it.hasNext()) {
            String code = it.next();
            String val = json.getString(code);
            boolean isJson = false;
            try {
                JSONObject.fromObject(val);
                isJson = true;
            } catch (Exception e) {
            }
            if (isJson) {
                saveKey(outKey + "." + code, json.getString(code));
            } else {
                customConfigDao.saveVal(outKey + "." + code, null);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void saveVal(String code, String value) {
        customConfigDao.saveVal(code, value);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateVal(String code, String value) {
        customConfigDao.updateVal(code, value);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map<String, Object> findValsByDefCode(String defCode) {
        return customConfigDao.findValsByDefCode(defCode);
    }


}
