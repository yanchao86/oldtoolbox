package com.pixshow.toolboxmgr.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.toolboxmgr.bean.DlDayStatBean;
import com.pixshow.toolboxmgr.bean.ToolboxBean;

@Repository
public class ToolboxDao extends BaseDao {

	/**
	 * 添加工具
	 */
	public int insertTool(ToolboxBean bean) {
		return insertBean("tb_toolbox", bean).intValue();
	}

	/**
	 * 添加包名表
	 */
	public void insertPackage(int toolboxId, String packageName, Date createDate) {
	    try {
	        insert("INSERT INTO tb_toolbox_package(toolboxId,packageName,createDate) VALUES(?,?,?)", toolboxId, packageName, createDate);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
	}

	/**
	 * 最大排序索引
	 */
	public int maxSortIndex() {
		return getJdbcTemplate().queryForInt("select max(sortIndex) from tb_toolbox");
	}

	/**
	 * 修改包名表
	 */
	public void updatePackage(int toolboxId, String packageName) {
		update("UPDATE tb_toolbox_package SET packageName=? where toolboxId=?", packageName, toolboxId);
	}

	/**
	 * 查询工具
	 */
	public List<ToolboxBean> searchTool() {
		return queryForList("SELECT * FROM tb_toolbox ORDER BY sortIndex", ToolboxBean.class);
	}

	/**
	 * 修改工具
	 */
	public void updateTool(ToolboxBean bean) {
		updateBean("tb_toolbox", bean, "id=" + bean.getId());
	}

	/**
	 * 通过ID查询工具
	 */
	public ToolboxBean searchByIDTool(int id) {
		return queryForBean("SELECT * from tb_toolbox WHERE id=?", ToolboxBean.class, id);
	}

	/**
	 * 删除工具
	 */
	public void deleteTool(int id) {
		update("DELETE FROM tb_toolbox WHERE id=?", id);
	}

	/**
	 * 工具排序
	 */
	public void sortTool(int id, int sortIndex) {
		update("UPDATE tb_toolbox SET sortIndex=? WHERE id=?", sortIndex, id);
	}

	public List<DlDayStatBean> dlDayStats(int toolId) {
		ToolboxBean toolboxBean = queryForBean("select * from tb_toolbox where id=?", ToolboxBean.class, toolId);
		if (toolboxBean == null) {
			return new ArrayList<DlDayStatBean>();
		} else {
			String packageName = toolboxBean.getPackageName();
//			String sql = "SELECT day, SUM(count) as count FROM tb_toolbox_day_stat WHERE code LIKE ? AND code LIKE '%ibox_apps_download_finish_num' GROUP BY day ORDER BY day DESC";
			String sql = "SELECT day, SUM(count) as count FROM tb_toolbox_day_stat WHERE code LIKE ? AND code LIKE '%toolbox_download' GROUP BY day ORDER BY day DESC";
			List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, packageName + '%');
			List<DlDayStatBean> beans = new ArrayList<DlDayStatBean>();
			DlDayStatBean bean;
			boolean flag = true;
			for (Map<String, Object> map : list) {
				BigDecimal bigDecimal = (BigDecimal) map.get("count");
				int count = bigDecimal.intValue();
				bean = new DlDayStatBean();
				if(flag){
					bean.setName(toolboxBean.getName());
					flag=false;
				}
				bean.setDay((Integer) map.get("day"));
				bean.setCount(count);
				beans.add(bean);
			}
			return beans;

//			return queryForList(sql, DlDayStatBean.class, packageName + '%');
		}
		/*
		 * return queryForList("select d.*, t.* from tb_toolbox_dl_day_stat d"//
		 * + " join tb_toolbox t on t.id=d.toolId"// +
		 * " where d.toolId=? order by d.day desc", DlDayStatBean.class,
		 * toolId);
		 */
	}

	/**
	 * 添加回收站
	 */
	public void insertRecycle(ToolboxBean bean) {
		insertBean("tb_toolbox_recycle", bean);
	}

	/**
	 * 返回所有回收站里的内容
	 * 
	 * @return
	 */
	public List<ToolboxBean> findAllRecycle() {
		return queryForList("SELECT * FROM tb_toolbox_recycle ORDER BY sortIndex", ToolboxBean.class);
	}

	/**
	 * 根据ID还原回收站
	 * 
	 * @param id
	 */
	public void setlctInto(int id) {
		update("INSERT INTO tb_toolbox SELECT * FROM tb_toolbox_recycle AS tr WHERE tr.id=?", id);
		update("DELETE FROM tb_toolbox_recycle WHERE id=?", id);
	}

}
