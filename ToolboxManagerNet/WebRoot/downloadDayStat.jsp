<%@page import="com.pixshow.toolboxmgr.bean.DlDayStatBean"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String basePath = WebUtility.getBasePath(request);
	Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");
	List<DlDayStatBean> downloadDayStats = (List<DlDayStatBean>) result.get("downloadDayStats");
	if(downloadDayStats.size() == 0) {
	    out.print("暂无下载量！");
	    return;
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
	</head>

	<body>
		<table id="customers" width="98%">
			<tr>
				<td colspan="2"><%=downloadDayStats.get(0).getName() %></td>
			</tr>  
			<tr>
				<th>日期</th>
				<th>下载量</th>
			</tr>
		<%for(DlDayStatBean day : downloadDayStats) {%>
			<tr>
				<td><%=day.getDay() %></td>
				<td><%=day.getCount() %></td>
			</tr>  
		<%} %>
		</table>
	</body>
</html>
