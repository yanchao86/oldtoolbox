package com.pixshow.toolboxmgr.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.toolboxmgr.bean.DiyboxBean;
import com.pixshow.toolboxmgr.bean.DlDayStatBean;

@Repository
public class DiyboxDao extends BaseDao {

    /**
     * 添加工具
     */
    public int insertTool(DiyboxBean bean) {
        return insertBean("tb_diybox", bean).intValue();
    }

    /**
     * 添加包名表
     */
    public void insertPackage(int diyboxId, String packageName, Date createDate) {
        insert("INSERT INTO tb_diybox_package(diyboxId,packageName,createDate) VALUES(?,?,?)", diyboxId, packageName, createDate);
    }

    /**
     * 修改包名表
     */
    public void updatePackage(int diyboxId, String packageName) {
        update("UPDATE tb_diybox_package SET packageName=? where diyboxId=?", packageName, diyboxId);
    }

    /**
     * 查询工具
     */
    public List<DiyboxBean> searchTool() {
        return queryForList("SELECT * FROM tb_diybox ORDER BY sortIndex", DiyboxBean.class);
    }

    /**
     * 修改工具
     */
    public void updateTool(DiyboxBean bean) {
        updateBean("tb_diybox", bean, "id=" + bean.getId());
    }

    /**
     * 通过ID查询工具
     */
    public DiyboxBean searchByIDTool(int id) {
        return queryForBean("SELECT * from tb_diybox WHERE id=?", DiyboxBean.class, id);
    }

    /**
     * 删除工具
     */
    public void deleteTool(int id) {
        update("DELETE FROM tb_diybox WHERE id=?", id);
    }

    /**
     * 添加回收站
     */
    public void insertRecycle(DiyboxBean bean) {
        insertBean("tb_diybox_recycle", bean);
    }

    /**
     * 工具排序
     */
    public void sortTool(int id, int sortIndex) {
        update("UPDATE tb_diybox SET sortIndex=? WHERE id=?", sortIndex, id);
    }

    public List<DlDayStatBean> dlDayStats(int toolId) {
        return queryForList("select d.*, t.* from tb_diybox_dl_day_stat d"//
                + " join tb_diybox t on t.id=d.toolId"//
                + " where d.toolId=? order by d.day desc", DlDayStatBean.class, toolId);
    }
}
