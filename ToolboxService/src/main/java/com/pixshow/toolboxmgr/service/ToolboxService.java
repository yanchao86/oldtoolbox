package com.pixshow.toolboxmgr.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pixshow.framework.config.Config;
import com.pixshow.framework.support.BaseService;
import com.pixshow.framework.utils.DateUtility;
import com.pixshow.toolboxmgr.bean.ToolboxBean;
import com.pixshow.toolboxmgr.dao.ToolboxDao;
import com.pixshow.toolboxmgr.tools.ImageStorageTootl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ToolboxService extends BaseService {
    @Resource
    private ToolboxDao toolboxDao;

    /**
     * 查询工具
     */
    public List<ToolboxBean> searchTool(int index, int items) {
        return toolboxDao.searchTool(index, items);
    }

    /**
     * 通过ID查询工具
     */
    public ToolboxBean searchByIDTool(int id) {
        return toolboxDao.searchByIDTool(id);
    }

    public List<ToolboxBean> searchToolByIds(List<Integer> ids) {
        return toolboxDao.searchToolByIds(ids);
    }

    public List<Map<String, Object>> allPackageName() {
        return toolboxDao.allPackageName();
    }

    public JSONArray searchToolsUpdate(int index, int items) {
        List<ToolboxBean> list = this.searchTool(index, items);
        JSONArray result = new JSONArray();
        if (list == null) {
            log.info("searchTool can not find result!");
            return null;
        }
        if (list.size() <= 0) {
            log.info("find result list,but it is empty!");
            return result;
        }
        for (ToolboxBean bean : list) {
            JSONObject json = new JSONObject();
            json.put("id", bean.getId());
            json.put("name", bean.getName());
            json.put("sortIndex", bean.getSortIndex());
            json.put("icon", ImageStorageTootl.getUrl(bean.getIcon()));
            json.put("downloadUrl", Config.getInstance().getString("toolbox.download.baseUrl") + "service/download.do?appId=" + bean.getId());
            json.put("downloadAuto", bean.getDownloadAuto() == 0 ? false : true);
            json.put("downloadCount", bean.getDownloadCount());
            json.put("detailUrl", bean.getDetailUrl());
            json.put("detailOpen", bean.getDetailOpen() == 0 ? false : true);
            json.put("rate", bean.getRate());
            json.put("extInfo1", bean.getExtInfo1());
            json.put("extInfo2", bean.getExtInfo2());
            json.put("extInfo3", bean.getExtInfo3());
            json.put("packageName", bean.getPackageName());
            json.put("versionCode", bean.getVersionCode());
            json.put("createDate", DateUtility.format(bean.getCreateDate()));
            json.put("updateDate", DateUtility.format(bean.getUpdateDate()));
            result.add(json);
        }
        return result;
    }
}
