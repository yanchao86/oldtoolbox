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

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pixshow.framework.schedule.api.annotation.ScheduleJob;
import com.pixshow.framework.utils.DateUtility;
import com.pixshow.framework.utils.MailUtility;
import com.pixshow.framework.utils.StringUtility;
import com.pixshow.toolboxmgr.bean.StatWarningBean;
import com.pixshow.toolboxmgr.service.StatCodeService;
import com.pixshow.toolboxmgr.service.StatWarningService;

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
@ScheduleJob(name = "StatWarningJob", cron = "0 0 7 * * ?")
public class StatWarningJob implements Job {
    @Autowired
    private StatWarningService        statWarningService;
    @Autowired
    private StatCodeService           statCodeService;

    private Map<String, JSONObject>   codeMap   = new HashMap<String, JSONObject>();
    private Map<String, List<String>> emailMap  = new HashMap<String, List<String>>();
    private List<Map<String, Object>> codeNames = new ArrayList<Map<String, Object>>();

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        List<StatWarningBean> allWarnings = statWarningService.findAll();
        codeNames = statCodeService.findKeyValue();
        for (StatWarningBean warning : allWarnings) {
            getData(warning);
            String email = warning.getEmail();
            String[] emails = email.split("(,|，|;|；|\\s)");
            for (String mail : emails) {
                if (StringUtility.isEmpty(mail)) {
                    continue;
                }
                List<String> codes = emailMap.containsKey(mail) ? emailMap.get(mail) : new ArrayList<String>();
                if (!codes.contains(warning.getCode())) {
                    codes.add(warning.getCode());
                }
                emailMap.put(mail, codes);
            }
        }
        sendMail();
    }

    private int[] week_date = new int[8];

    private void getData(StatWarningBean warning) {
        JSONObject data = new JSONObject();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -1);
        week_date[0] = Integer.parseInt(DateUtility.format(c, "yyyyMMdd"));

        for (int i = 1; i <= 7; i++) {
            c.add(Calendar.DAY_OF_MONTH, -1);
            week_date[i] = Integer.parseInt(DateUtility.format(c, "yyyyMMdd"));
        }
        int yesterday = 0;
        int week_count = 0;
        List<Map<String, Object>> stats = statCodeService.statQuery("WHERE `code`=? AND `day` BETWEEN ? AND ?", warning.getCode(), week_date[7], week_date[0]);
        if (stats.size() == 0) { return; }
        for (Map<String, Object> map : stats) {
            int day = (Integer) map.get("day");
            int count = (Integer) map.get("count");
            if (day == week_date[0]) {
                yesterday = count;
            } else {
                week_count += count;
            }
            data.put(day + "", count);
        }
        int average = week_count / 7;
        int result = average != 0 ? (yesterday - average) * 100 / average : 0;

        data.put("average", average);
        data.put("result", result);
        data.put("upThreshold", warning.getUpThreshold());
        data.put("downThreshold", warning.getDownThreshold());
        codeMap.put(warning.getCode(), data);
    }

    private JSONObject getHtml(List<String> codes) {
        JSONObject html = new JSONObject();
        if (codes == null || codes.size() == 0) { return html; }
        StringBuilder table = new StringBuilder();
        table.append("<style type='text/css'>.custom_stat_table td{white-space:nowrap;}</style>");
        table.append("<table class='custom_stat_table'>");
        table.append("<tr bgcolor='#77DDFF' align='center'>");
        table.append("<td>").append("序号").append("</td>");
        table.append("<td>").append("指数参考").append("</td>");
        table.append("<td>").append("名称").append("</td>");
        table.append("<td>").append("code").append("</td>");
        table.append("<td>").append(week_date[0]).append("</td>");
        table.append("<td>").append("七天平均").append("</td>");
        table.append("<td>").append("对比").append("</td>");
        for (int j = 1; j < week_date.length; j++) {
            int week = week_date[j];
            table.append("<td>").append(week).append("</td>");
        }
        table.append("</tr>");
        for (int i = 0; i < codes.size(); i++) {
            String code = codes.get(i);
            String codeName = code;
            for (Map<String, Object> keyMap : codeNames) {
                String name = (String) keyMap.get("name");
                String value = (String) keyMap.get("value");
                codeName = codeName.replace(name, value);
            }
            JSONObject data = codeMap.get(code);
            data = data == null ? new JSONObject() : data;
            int result = data.optInt("result");
            table.append("<tr bgcolor='" + (i % 2 == 0 ? "" : "#DDDDDD") + "'>");
            table.append("<td>").append(i + 1).append("</td>");

            table.append("<td>").append(getJiantou(result)).append("</td>");
            table.append("<td>").append(codeName).append("</td>");
            table.append("<td>").append(code).append("</td>");
            table.append("<td>").append(data.optInt(week_date[0] + "")).append("</td>");
            table.append("<td>").append(data.optInt("average")).append("</td>");
            table.append("<td bgcolor='" + (result > 0 ? "#FF8800" : result < 0 ? "#DDFF66" : "") + "'>").append(data.optInt("result") + "%").append("</td>");
            for (int j = 1; j < week_date.length; j++) {
                int week = week_date[j];
                table.append("<td>").append(data.optInt(week + "")).append("</td>");
            }

            table.append("</tr>");
        }
        table.append("</table>");
        html.put("content", table.toString());
        return html;
    }

    private String getJiantou(int result) {
        int count = Math.abs(result) / 5;
        StringBuilder jt = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 19) {
                jt.append("...");
                break;
            }
            if (result > 0) {
                jt.append("<img src=\"cid:up\" />");
            } else if (result < 0) {
                jt.append("<img src=\"cid:down\" />");
            }
        }
        return jt.toString();
    }

    private void sendMail() {
        MailUtility.Mail mail = new MailUtility.Mail();
        mail.user = "apps@pixshow.net";
        mail.password = "lvtu1233211";
        mail.server = "smtp.exmail.qq.com";
        try {
            mail.attachment.put("down", new File(getClass().getResource("/img/down.gif").toURI()));
            mail.attachment.put("up", new File(getClass().getResource("/img/up.gif").toURI()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Iterator<String> it = emailMap.keySet().iterator();
        while (it.hasNext()) {
            String email = it.next();
            List<String> codes = emailMap.get(email);
            JSONObject data = getHtml(codes);
            mail.to = email;
            mail.title = "↑↓" + DateUtility.format(new Date(), "yyyy-MM-dd") + "统计增减指数";
            mail.content = data.optString("content");
            MailUtility.sendSingle(mail);
        }
    }

    /**
     * 420666366@qq.com 27645806@qq.com 165343918@qq.com 36387357@qq.com
     * 1182952194@qq.com 675502336@qq.com 5575000@qq.com 807602686@qq.com
     * 
     * @param args
     */
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:/config/framework/spring.xml");
        StatWarningJob job = context.getBean(StatWarningJob.class);
        try {
            job.execute(null);
        } catch (JobExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
