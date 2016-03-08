package com.pixshow.toolboxmgr.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pixshow.framework.support.BaseService;
import com.pixshow.toolboxmgr.dao.StatCodeDao;

@Service
public class StatCodeService extends BaseService {
    @Autowired
    private StatCodeDao statCodeDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> statByCode(String code) {
        return statCodeDao.statByCode(code);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> statByLikeCode(String code) {
        return statCodeDao.statByLikeCode(code);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> statWeekByCode(List<Object> codes) {
        return statCodeDao.statWeekByCode(codes);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> statCountByCode(List<Object> codes) {
        return statCodeDao.statCountByCode(codes);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> findAllDayStatCodes() {
        return statCodeDao.findAllDayStatCodes();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void insertCat(String name) {
        statCodeDao.insertCat(name);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateCatCodeName(int codeId, String name) {
        statCodeDao.updateCatCodeName(codeId, name);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void insertCatCode(int catId, String name, String code) {
        statCodeDao.insertCatCode(catId, name, code);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteCat(int catId) {
        statCodeDao.deleteCat(catId);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteCatCode(int codeId) {
        statCodeDao.deleteCatCode(codeId);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteCatCodes(String statCatCodeIds) {
        statCodeDao.deleteCatCodes(statCatCodeIds);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map<String, Object> findCatById(int catId) {
        List<Map<String, Object>> cats = statCodeDao.findCats("where id=?", catId);
        return cats.size() > 0 ? cats.get(0) : null;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> findAllCats() {
        return statCodeDao.findCats("", null);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map<String, Object> findCatCodeById(int codeId) {
        List<Map<String, Object>> cats = statCodeDao.findCatCodes("where id=?", codeId);
        return cats.size() > 0 ? cats.get(0) : null;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> findCatCodes(int catId) {
        return statCodeDao.findCatCodes("where catId=?", catId);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> findCodesNotInCatIdAndLikeByCode(int catId, String code) {
        return statCodeDao.findCodesNotInCatIdAndLikeByCode(catId, code);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> findKeyValue() {
        return statCodeDao.findKeyValue("order by length(name) desc", null);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> findKeyValueOrderById() {
        return statCodeDao.findKeyValue("order by id desc", null);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void insertKeyValue(String name, String value) {
        statCodeDao.insertKeyValue(name, value);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateKeyValue(int id, String value) {
        statCodeDao.updateKeyValue(id, value);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteKeyValue(int id) {
        statCodeDao.deleteKeyValue(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> statQuery(String sql, Object... param) {
        return statCodeDao.statQuery(sql, param);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Map<String, Object>> statByCodeAndDay(String prefixCode, String suffixCode, int startDay, int endDay) {
        return statCodeDao.statByCodeAndDay(prefixCode, suffixCode, startDay, endDay);
    }
}
