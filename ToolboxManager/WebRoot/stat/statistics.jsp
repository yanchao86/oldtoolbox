<%@page import="java.net.URLEncoder"%>
<%@page import="com.pixshow.framework.utils.HttpUtility"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="org.apache.commons.lang.math.NumberUtils"%>
<%@page import="com.pixshow.framework.utils.StringUtility"%>
<%@page import="com.pixshow.framework.utils.DateUtility"%>
<%@page import="com.pixshow.framework.utils.AppContextUtility"%>
<%@page import="com.pixshow.toolboxmgr.service.StatCodeService"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);

int date_length = StringUtility.isEmpty(request.getParameter("date_length"))?0:Integer.parseInt(request.getParameter("date_length"));
int date_max = Integer.parseInt(DateUtility.format(new Date(), "yyyyMMdd"));
Date d = DateUtility.parseDate(date_max+"", "yyyyMMdd");
Calendar c = Calendar.getInstance();
c.setTime(d);
c.add(Calendar.DAY_OF_MONTH, 0-date_length);
int date_min = Integer.parseInt(DateUtility.format(c, "yyyyMMdd"));

init(date_min, date_max);
//--------------------------

TreeMap<JSONObject, List<Map<String, Object>>> result = new TreeMap<JSONObject, List<Map<String, Object>>>();


JSONObject gamehelper = new JSONObject();
gamehelper.put("name", "7 游戏加速器"); gamehelper.put("code", "_com.gangclub.gamehelper_");
result.put(gamehelper, getResult("_com.gangclub.gamehelper_"));

JSONObject clock = new JSONObject();
clock.put("name", "6 时钟"); clock.put("code", "_com.dotools.clock_");
result.put(clock, getResult("_com.dotools.clock_"));

JSONObject weathercamera = new JSONObject();
weathercamera.put("name", "5 天气相机"); weathercamera.put("code", "_com.ejnet.weathercamera_");
result.put(weathercamera, getResult("_com.ejnet.weathercamera_"));

JSONObject weather = new JSONObject();
weather.put("name", "4 即时天气"); weather.put("code", "_com.dotools.weather_");
result.put(weather, getResult("_com.dotools.weather_"));

JSONObject calculators = new JSONObject();
calculators.put("name", "3 计算器"); calculators.put("code", "_com.ibox.calculators_");
result.put(calculators, getResult("_com.ibox.calculators_"));

JSONObject flashlight = new JSONObject();
flashlight.put("name", "2 手电筒"); flashlight.put("code", "_com.ibox.flashlight_");
result.put(flashlight, getResult("_com.ibox.flashlight_"));

JSONObject all = new JSONObject();
all.put("name", "1 全部"); all.put("code", "");
result.put(all, getResult(null));

%>

<%!
//点击下载
private List<Map<String, Object>> dianji = new ArrayList<Map<String, Object>>();
private List<Map<String, Object>> dianji_tool = new ArrayList<Map<String, Object>>();
//下载开始
private List<Map<String, Object>> xiazai = new ArrayList<Map<String, Object>>();
//下载完成
private List<Map<String, Object>> xiazaihao = new ArrayList<Map<String, Object>>();
//安装完成
private List<Map<String, Object>> anzhuang = new ArrayList<Map<String, Object>>();

private StatCodeService statCodeService = AppContextUtility.getContext().getBean(StatCodeService.class);
private void init(int date_min, int date_max) {
    dianji = statCodeService.statQuery("WHERE `code` LIKE '%FRAME_PV' AND `day` BETWEEN ? AND ?", date_min, date_max);
    dianji_tool = statCodeService.statQuery("WHERE `code` LIKE '%listitem_download' AND `day` BETWEEN ? AND ?", date_min, date_max);
    xiazai = statCodeService.statQuery("WHERE `code` LIKE '%download_start_num' AND `day` BETWEEN ? AND ?", date_min, date_max);
    xiazaihao = statCodeService.statQuery("WHERE `code` LIKE '%download_finish_num' AND `day` BETWEEN ? AND ?", date_min, date_max);
    anzhuang = statCodeService.statQuery("WHERE `code` LIKE '%install_finish_num' AND `day` BETWEEN ? AND ?", date_min, date_max);
}

private List<Map<String, Object>> getResult(String proKey) {
    proKey = proKey==null?"":proKey;
    List<Map<String, Object>> all = new ArrayList<Map<String, Object>>();
    Map<String, Object> map_1 = new HashMap<String, Object>();
    map_1.put("name", "通知栏虚框");
    map_1.put("dj_count", getCount(dianji, proKey+"FRAME_PV"));
    map_1.put("xz_count", getCount(xiazai, proKey+"frame_download_start_num"));
    map_1.put("xzh_count", getCount(xiazaihao, proKey+"frame_download_finish_num"));
    map_1.put("xzh_auto_count", getCount(xiazaihao, proKey+"auto_apps_download_finish_num"));

    Map<String, Object> map_2 = new HashMap<String, Object>();
    map_2.put("name", "通知栏的工具箱");
    map_2.put("dj_count", getCount(dianji_tool, proKey+"toolbox_from_notifcation_listitem_download"));
    map_2.put("xz_count", getCount(xiazai, proKey+"notifcation_apps_download_start_num", proKey+"notifcation_dotools_download_start_num"));
    map_2.put("xzh_count", getCount(xiazaihao, proKey+"notifcation_apps_download_finish_num", proKey+"notifcation_dotools_download_finish_num"));

    Map<String, Object> map_3 = new HashMap<String, Object>();
    map_3.put("name", "应用内的工具箱");
    map_3.put("dj_count", getCount(dianji_tool, proKey+"toolbox_from_apps_listitem_download"));
    map_3.put("xz_count", getCount(xiazai, proKey+"ibox_apps_download_start_num", proKey+"ibox_dotools_download_start_num"));
    map_3.put("xzh_count", getCount(xiazaihao, proKey+"ibox_apps_download_finish_num", proKey+"ibox_dotools_download_finish_num"));
	
    Map<String, Object> map_4 = new HashMap<String, Object>();
    map_4.put("az_cout", getCount(anzhuang, proKey+"install_finish_num"));
    
    all.add(map_1);
    all.add(map_2);
    all.add(map_3);
    all.add(map_4);
    return all;
}

private int getCount(List<Map<String, Object>> data, String... key) {
    int result = 0;
    for(Map<String, Object> d : data) {
        String code = (String) d.get("code");
        int count = (Integer) d.get("count");
        for(int j=0; j<key.length; j++) {
	        if(code.indexOf(key[j]) > 0) {
	            result += count;
	        }
        }
    }
    return result;
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
	<script type="text/javascript">
		function stat(date_length) {
			window.location.href="<%=basePath%>stat/statistics.jsp?date_length="+date_length;
		}
	</script>
</head>
<body>
	<div style="width: 100%; text-align: center;">
		<button onclick="stat(0)" style="background-color: <%=date_length==0?"red":""%>">今天的数据</button>
		<button onclick="stat(7)" style="background-color: <%=date_length==7?"red":""%>">7天内数据</button>
		<button onclick="stat(30)" style="background-color: <%=date_length==30?"red":""%>">30天内数据</button>
	</div>

<%
Iterator<JSONObject> it = result.keySet().iterator();
while(it.hasNext()) {
    JSONObject data = it.next();
    String pName = data.optString("name");
    String pCode = data.optString("code");
    %>
<table id="customers" align="center" width="100%">
	<tr>
		<td width="20%"><h3><a href="<%=basePath %>stat/statisticsInfo.jsp?pCode=<%=pCode %>&pName=<%=URLEncoder.encode(pName, "UTF-8") %>" title="30天详细"><%=pName %></a></h3></td>
		<th width="20%">点击数</th>
		<th width="20%">下载开始</th>
		<th width="20%">下载完成</th>
		<th width="20%">安装完成</th>
	</tr>    
    <%
    List<Map<String, Object>> value = result.get(data);
    if(value == null || value.size() == 0) {%>
    <tr>
    	<td colspan="5"><font color="red">--暂无数据--</font></td>
    </tr>
    <%} else {
		int az_cout = value.get(value.size()-1).get("az_cout")==null?0:(Integer)value.get(value.size()-1).get("az_cout");
		for(int i=0; i<value.size()-1; i++) {
			Map<String, Object> rs = value.get(i);
		    String name = (String) rs.get("name") ;
			int dj_count = rs.get("dj_count")==null?0:(Integer)rs.get("dj_count");
			int xz_count = rs.get("xz_count")==null?0:(Integer)rs.get("xz_count");
			int xzh_count = rs.get("xzh_count")==null?0:(Integer)rs.get("xzh_count");
			int xzh_auto_count = rs.get("xzh_auto_count")==null?0:(Integer)rs.get("xzh_auto_count");
%>
	<tr>
		<th><%=name %></th>
		<td><%=dj_count %></td>
		<td><%=xz_count %><%=dj_count!=0?"("+(xz_count*100/dj_count)+"%)":"" %></td>
		<td><%=xzh_count %><%=xz_count!=0?"("+(xzh_count*100/xz_count)+"%)":"" %><%=name.equals("通知栏虚框")?"(预下载："+xzh_auto_count+")":"" %></td>
		<%if(i==0) {%>
		<td rowspan="4"><%=az_cout %></td>
		<%}%>
	</tr>
	<%}
	}%>
</table>
<br>
<%}%>
		
</body>
</html>