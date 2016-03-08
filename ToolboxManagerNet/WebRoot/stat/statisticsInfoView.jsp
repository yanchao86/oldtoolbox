<%@page import="java.net.URLDecoder"%>
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
request.setCharacterEncoding("UTF-8");
String basePath = WebUtility.getBasePath(request);
String pName = request.getParameter("pName");
//pName = new String(pName.getBytes("ISO-8859-1"), "UTF-8");
String pCode = request.getParameter("pCode");

int date_length = 30;
int date_max = Integer.parseInt(DateUtility.format(new Date(), "yyyyMMdd"));
Date d = DateUtility.parseDate(date_max+"", "yyyyMMdd");
Calendar c = Calendar.getInstance();
c.setTime(d);
c.add(Calendar.DAY_OF_MONTH, 0-date_length);
int date_min = Integer.parseInt(DateUtility.format(c, "yyyyMMdd"));

init(date_min, date_max, pCode);
//--------------------------

TreeMap<String, List<Map<String, Object>>> result = new TreeMap<String, List<Map<String, Object>>>();

List<Map<String, Object>> data = getResult(pCode);
%>

<%!
//点击下载
private List<Map<String, Object>> code_stat = new ArrayList<Map<String, Object>>();

private StatCodeService statCodeService = AppContextUtility.getContext().getBean(StatCodeService.class);
private void init(int date_min, int date_max, String pCode) {
    code_stat = statCodeService.statQuery("WHERE `code` LIKE '%\\"+pCode+"%' AND `day` BETWEEN ? AND ?", date_min, date_max);
}

private List<Map<String, Object>> getResult(String pCode) {
    List<Map<String, Object>> all = new ArrayList<Map<String, Object>>();
    Map<String, Object> map_1 = new HashMap<String, Object>();
    map_1.put("name", "通知栏虚框");
    map_1.put("dj_count", getCount(code_stat, pCode+"FRAME_PV"));
    map_1.put("xz_count", getCount(code_stat, pCode+"frame_download_start_num"));
    map_1.put("xzh_count", getCount(code_stat, pCode+"frame_download_finish_num"));
    map_1.put("xzh_auto_count", getCount(code_stat, pCode+"auto_apps_download_finish_num"));

    Map<String, Object> map_2 = new HashMap<String, Object>();
    map_2.put("name", "通知栏的工具箱");
    map_2.put("dj_count", getCount(code_stat, pCode+"toolbox_from_notifcation_listitem_download"));
    map_2.put("xz_count", getCount(code_stat, pCode+"notifcation_apps_download_start_num", pCode+"notifcation_dotools_download_start_num"));
    map_2.put("xzh_count", getCount(code_stat, pCode+"notifcation_apps_download_finish_num", pCode+"notifcation_dotools_download_finish_num"));

    Map<String, Object> map_3 = new HashMap<String, Object>();
    map_3.put("name", "应用内的工具箱");
    map_3.put("dj_count", getCount(code_stat, pCode+"toolbox_from_apps_listitem_download"));
    map_3.put("xz_count", getCount(code_stat, pCode+"ibox_apps_download_start_num", pCode+"ibox_dotools_download_start_num"));
    map_3.put("xzh_count", getCount(code_stat, pCode+"ibox_apps_download_finish_num", pCode+"ibox_dotools_download_finish_num"));
	
    Map<String, Object> map_4 = new HashMap<String, Object>();
    map_4.put("az_cout", getCount(code_stat, pCode+"install_finish_num"));
    
    all.add(map_1);
    all.add(map_2);
    all.add(map_3);
    all.add(map_4);
    return all;
}

private Map<String, Integer> getCount(List<Map<String, Object>> data, String... key) {
    Map<String, Integer> result = new HashMap<String, Integer>();
    for(Map<String, Object> d : data) {
        String code = (String) d.get("code");
        int day = (Integer) d.get("day");
        int count = (Integer) d.get("count");
        for(int j=0; j<key.length; j++) {
	        if(code.indexOf(key[j]) > 0) {
	            if(result.containsKey(day+"")) {
	                count = result.get(day+"")+count;
	            }
	            result.put(day+"", count);
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

<h3><%=pName %></h3>
<table id="customers" align="center" width="100%">
	<tr>
		<th width="20%">日期</th>
		<th width="20%">统计栏位</th>
		<th width="20%">点击数</th>
		<th width="20%">下载开始</th>
		<th width="20%">下载完成</th>
		<th width="20%">安装完成</th>
	</tr>
<%
Calendar cf = Calendar.getInstance();
for(int m=0; m<date_length; m++) {
	String dateStr = DateUtility.format(cf, "yyyyMMdd");
	cf.add(Calendar.DAY_OF_MONTH, -1);
    
	Map<String, Integer> az_cout = (Map<String, Integer>)data.get(data.size()-1).get("az_cout");
	for(int i=0; i<data.size()-1; i++) {
		Map<String, Object> rs = data.get(i);
	    String name = (String) rs.get("name") ;
	    Map<String, Integer> dj_count = rs.containsKey("dj_count")?(Map<String, Integer>)rs.get("dj_count"):new HashMap<String, Integer>();
	    Map<String, Integer> xz_count = rs.containsKey("xz_count")?(Map<String, Integer>)rs.get("xz_count"):new HashMap<String, Integer>();
	    Map<String, Integer> xzh_count = rs.containsKey("xzh_count")?(Map<String, Integer>)rs.get("xzh_count"):new HashMap<String, Integer>();
	    Map<String, Integer> xzh_auto_count = rs.containsKey("xzh_auto_count")?(Map<String, Integer>)rs.get("xzh_auto_count"):new HashMap<String, Integer>();
	    
	    int dj_count_int = dj_count.containsKey(dateStr)?dj_count.get(dateStr):0;
	    int xz_count_int = xz_count.containsKey(dateStr)?xz_count.get(dateStr):0;
	    int xzh_count_int = xzh_count.containsKey(dateStr)?xzh_count.get(dateStr):0;
	    int xzh_auto_count_int = xzh_auto_count.containsKey(dateStr)?xzh_auto_count.get(dateStr):0;
	    
%>
	<tr bgcolor="<%=m%2==0?"":"#DDDDDD"%>">
		<%if(i==0) {%>
		<td rowspan="3"><%=dateStr %></td>
		<%} %>
		<td><%=name %></td>
		<td><%=dj_count_int %></td>
		<td>
			<%=xz_count_int %>
			<%=dj_count_int!=0?"("+(xz_count_int*100/dj_count_int)+"%)":"" %>
		</td>
		<td>
			<%=xzh_count_int %>
			<%=xz_count_int!=0?"("+(xzh_count_int*100/xz_count_int)+"%)":"" %>
			<%=name.equals("通知栏虚框")?"(预下载："+xzh_auto_count_int+")":"" %>
		</td>
		<%if(i==0) {%>
		<td rowspan="3"><%=az_cout.containsKey(dateStr)?az_cout.get(dateStr):0 %></td>
		<%}%>
	</tr>
	<%}
}%>
</table>
<br>
		
</body>
</html>