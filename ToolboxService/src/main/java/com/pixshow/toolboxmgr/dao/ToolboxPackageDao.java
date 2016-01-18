package com.pixshow.toolboxmgr.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.toolboxmgr.bean.ToolboxPackageBean;

@Repository
public class ToolboxPackageDao extends BaseDao {
    /**
     * 查询包名表
     */
    public List<ToolboxPackageBean> searchPackage() {
        return queryForList("SELECT * FROM tb_toolbox_package ORDER BY toolboxId limit 0, 300", ToolboxPackageBean.class);

    }
}
