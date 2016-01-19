package com.pixshow.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pixshow.toolboxmgr.service.PropertiesService;
import com.pixshow.toolboxmgr.service.ToolboxService;

@Component
public class Mysql2RedisTask {
	
    @Autowired
    private ToolboxService      toolboxService;
    @Autowired
    private PropertiesService propertiesService;
	
//	@Scheduled(fixedRate = 60000)
	public void mysql2redis() {
		toolboxService.searchToolsUpdate2Redis();
		propertiesService.searchAppConfigArrayAndUpdate2Redis();
	}
}
