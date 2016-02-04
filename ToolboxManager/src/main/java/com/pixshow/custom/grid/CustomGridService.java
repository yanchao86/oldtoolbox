/*
 * Copyright (c) 2010-2014 www.pixshow.net All Rights Reserved
 *
 * File:CustomConfigDao.java Project: ToolboxManager
 * 
 * Creator:4399-lvtu-8 
 * Date:2014年3月7日 上午4:15:37
 * 
 */
package com.pixshow.custom.grid;

import java.util.List;
import java.util.Map;

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
public class CustomGridService extends BaseService {
    @Autowired
    private CustomGridDao customGridDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void saveGrid(String code, String name, String definition) {
        customGridDao.saveGrid(code, name, definition);
        customGridDao.createTable(definition);
    }

    //TODO 待修改
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateGrid(String code, String name, String definition) {
        Map<String, Object> grid = findGridByCode(code);
        if (grid != null) {
            customGridDao.updateGrid(code, name, definition);
            customGridDao.alterTable(grid.get("definition").toString(), definition);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map<String, Object> findGridByCode(String code) {
        return customGridDao.findGridByCode(code);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> gridCodeList() {
        return customGridDao.gridCodeList();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> gridList() {
        return customGridDao.gridList();
    }
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> allGridTable() {
        return customGridDao.allGridTable();
    }

}
