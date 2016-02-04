package com.pixshow.toolboxmgr.action;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.jf.smali.smaliParser.integer_literal_return;
import org.python.antlr.PythonParser.and_expr_return;
import org.python.antlr.PythonParser.else_clause_return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pixshow.framework.support.BaseAction;
import com.pixshow.framework.utils.DateUtility;
import com.pixshow.toolboxmgr.service.PropertiesService;
import com.pixshow.toolboxmgr.service.StatCodeService;

@Controller
@Scope("prototype")
@Namespace("/manager")
public class AppDownloadStatAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	@Autowired
	private StatCodeService statCodeService;
	@Autowired
	private PropertiesService propertiesService;

	private String code;
	private int monthNum = 1;
	private int daykey;
	private String queryType;

	public int getDaykey() {
		return daykey;
	}

	public void setDaykey(int daykey) {
		this.daykey = daykey;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	private Map<String, Object> result = new HashMap<String, Object>();

	@Action(value = "appDownloadStatList", results = { @Result(name = SUCCESS, location = "/stat/appDownloadStatList.jsp") })
	public String appDownloadStatList() {
		List<String> apps = propertiesService.getValue("APP1_CONFIG", "APP2_CONFIG", "APP3_CONFIG", "APP4_CONFIG", "APP5_CONFIG");
		result.put("apps", apps);
		return SUCCESS;
	}

	@Action(value = "everyday", results = { @Result(name = SUCCESS, location = "/stat/everyday.jsp") })
	public String everyday() {
		return SUCCESS;
	}

	@Action(value = "everydayData", results = { @Result(name = SUCCESS, type = "json", params = { "root", "result" }) })
	public String everydayData() {
		List<Map<String, Object>> iBoxOpen = null;
		List<Map<String, Object>> notificationPV = null;
		List<Map<String, Object>> notificationUV = null;
		List<Map<String, Object>> appsPV = null;
		List<Map<String, Object>> appsUV = null;
		if ("iBoxOpen".equalsIgnoreCase(queryType)) {
			while ((iBoxOpen = propertiesService.getIBoxOpen(daykey)) == null || iBoxOpen.isEmpty()) {
			}
			result.put("iBoxOpen", iBoxOpen);
		} else if ("notificationPV".equalsIgnoreCase(queryType)) {
			while ((notificationPV = propertiesService.getNotificationPV(daykey)) == null || notificationPV.isEmpty()) {
			}
			result.put("notificationPV", notificationPV);
		} else if ("notificationUV".equalsIgnoreCase(queryType)) {
			while ((notificationUV = propertiesService.getNotificationUV(daykey)) == null || notificationUV.isEmpty()) {
			}
			result.put("notificationUV", notificationUV);
		} else if ("appsPV".equalsIgnoreCase(queryType)) {
			while ((appsPV = propertiesService.getAppsPV(daykey)) == null || appsPV.isEmpty()) {
			}
			result.put("appsPV", appsPV);
		} else if ("appsUV".equalsIgnoreCase(queryType)) {
			while ((appsUV = propertiesService.getAppsUV(daykey)) == null || appsUV.isEmpty()) {
			}
			result.put("appsUV", appsUV);
		}
		return SUCCESS;
	}

	@Action(value = "appDownloadStat", results = { @Result(name = SUCCESS, location = "/stat/appDownloadStat.jsp") })
	public String appDownloadStat() {
		int endDay = Integer.parseInt(DateUtility.format(new Date(), "yyyyMMdd"));
		int startDay = endDay;
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0 - monthNum);
		c.set(Calendar.DAY_OF_MONTH, 0);
		startDay = Integer.parseInt(DateUtility.format(c, "yyyyMMdd"));

		String prefixCode = code.substring(0, code.indexOf('@') == -1 ? code.length() : code.indexOf('@'));
		String suffixCode = code.substring(code.indexOf('@') == -1 ? code.length() : code.indexOf('@') + 1);

		List<Map<String, Object>> data = statCodeService.statByCodeAndDay(prefixCode, suffixCode, startDay, endDay);
		result.put("code", code);
		result.put("data", data);
		return SUCCESS;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getMonthNum() {
		return monthNum;
	}

	public void setMonthNum(int monthNum) {
		this.monthNum = monthNum;
	}

}
