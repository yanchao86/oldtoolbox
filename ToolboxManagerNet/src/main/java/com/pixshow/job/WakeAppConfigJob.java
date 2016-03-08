/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:WeatherRankJob.java Project: LvWeatherService
 * 
 * Creator:4399-lvtu-8 
 * Date:Dec 5, 2013 9:24:29 PM
 * 
 */
package com.pixshow.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;

import com.pixshow.framework.schedule.api.annotation.ScheduleJob;
import com.pixshow.toolboxmgr.service.PropertiesService;
import com.pixshow.toolboxmgr.tools.LargeDataPush;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$
 * @since Dec 5, 2013
 * 
 */
@PersistJobDataAfterExecution
@ScheduleJob(name = "WakeAppConfigJob", cron = "0 0 7 * * ?")
public class WakeAppConfigJob implements Job {
    @Autowired
    private PropertiesService propertiesService;

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        List<Map<String, Object>> values = propertiesService.likeValue("push.baidu");
        if (values == null || values.size() == 0) { return; }

        List<String> url = new ArrayList<String>();
        url.add("http://toolbox.tv163.com/service/appConfigArray.do");
        for (Map<String, Object> map : values) {
            JSONObject value = JSONObject.fromObject(map.get("value"));
            LargeDataPush.pushData(url, value.optString("appkey"), value.optString("secritkey"), "push2");
        }
    }

}
