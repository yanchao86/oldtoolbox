package com.pixshow.redis;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pixshow.toolboxmgr.service.PropertiesService;
import com.pixshow.toolboxmgr.service.ToolboxService;

import net.sf.json.JSONArray;

@Component
public class Mysql2RedisTask extends AbstractRedisService<String, String> {
    private final static Log log = LogFactory.getLog(ToolboxService.class);

    @Autowired
    private ToolboxService    toolboxService;
    @Autowired
    private PropertiesService propertiesService;

    @Scheduled(fixedRate = 1000 * 60 * 1)
    public void toolSearch() {
        int index = -1;
        int items = 9999;
        String rKey = "toolSearch@" + index + "_" + items;
        JSONArray result = toolboxService.searchToolsUpdate(index, items);
        try {
            this.set(rKey, result.toString());
        } catch (Exception e) {
            log.error("toolSearch redis set " + rKey + " error");
        }
        log.info("toolSearch redis set " + rKey + " end");
    }

    @Scheduled(fixedRate = 1000 * 60 * 2)
    public void searchAppConfigArray() {
        String[] keys = new String[] { "APP1_CONFIG", "APP2_CONFIG", "APP3_CONFIG", "APP4_CONFIG", "APP5_CONFIG" };
        String rKey = "appConfig@APP_CONFIG";
        List<String> list = propertiesService.getValue(keys);
        try {
            this.set(rKey, JSONArray.fromObject(list).toString());
        } catch (Exception e) {
            log.error("searchAppConfigArray redis set " + rKey + " error");
        }
        log.info("searchAppConfigArray redis set " + rKey + " end");
    }

}
