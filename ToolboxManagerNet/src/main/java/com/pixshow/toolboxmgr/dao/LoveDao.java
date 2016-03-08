package com.pixshow.toolboxmgr.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.toolboxmgr.bean.LoveBean;

@Repository
public class LoveDao extends BaseDao {

    /**
     * 添加爱心
     */
    public void insertLove(LoveBean bean) {
        insert("INSERT INTO tb_fl_love(name,image,description,createDate) VALUES(?,?,?,?)", bean.getName(), bean.getImage(), bean.getDescription(), bean.getCreateDate());
    }

    /**
     * 查询爱心列表
     */
    public List<LoveBean> searchLove() {
        return queryForList("SELECT * FROM tb_fl_love", LoveBean.class);
    }

    /**
     *删除爱心
     */
    public void deleteLove(int id) {
        update("DELETE FROM tb_fl_love WHERE id=?", id);
    }
}
