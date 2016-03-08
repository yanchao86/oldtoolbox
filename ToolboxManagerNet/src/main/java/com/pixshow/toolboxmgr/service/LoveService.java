package com.pixshow.toolboxmgr.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pixshow.framework.support.BaseService;
import com.pixshow.toolboxmgr.bean.LoveBean;
import com.pixshow.toolboxmgr.dao.LoveDao;

@Service
public class LoveService extends BaseService {
    @Resource
    private LoveDao loveDao;

    /**
     * 添加爱心
     */
    public void insertLove(LoveBean bean) {
        loveDao.insertLove(bean);
    }
    /**
     * 查询爱心列表
     */
    public List<LoveBean> searchLove() {
        return loveDao.searchLove();
    }
    /**
     *删除爱心
     */
    public void deleteLove(int id) {
        loveDao.deleteLove(id);
    }
}
