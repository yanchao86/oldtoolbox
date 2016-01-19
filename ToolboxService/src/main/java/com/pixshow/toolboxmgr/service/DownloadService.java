package com.pixshow.toolboxmgr.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pixshow.framework.support.BaseService;
import com.pixshow.framework.utils.DateUtility;
import com.pixshow.toolboxmgr.dao.DownloadDao;

@Service
public class DownloadService extends BaseService {
    private Map<String, Boolean> statistics;

    public DownloadService() {
        this.statistics = new HashMap();
    }

    @Resource
    private DownloadDao downloadDao;

    /**
     * 通过工具ID 查询下载地址
     */
    public String getUrl(String table, int toolId) {
        return downloadDao.getUrl(table, toolId);
    }

    /**
     * 通过工具ID 添加下载次数
     */
    public void addDownloadCount(String table, int toolId) {
        downloadDao.addDownloadCount(table, toolId);

        int day = Integer.parseInt(DateUtility.format(new Date(), "yyyyMMdd"));
        if (statistics.containsKey(toolId + "@" + table + "@" + day)) {
            downloadDao.updateDownloadCountDay(table, toolId, day);
        } else {
            try {
                downloadDao.addDownloadCountDay(table, toolId, day);
            } catch (Exception e) {
            }
            statistics.put(toolId + "@" + table + "@" + day, true);
        }
    }

    /**
     * 通过工具ID 添加下载次数
     */
    public void addDownloadCount(String table, int toolId, int day, int count) {
        downloadDao.addDownloadCount(table, toolId, count);
        downloadDao.saveDownloadCountDay(table, toolId, day, count);
    }

}
