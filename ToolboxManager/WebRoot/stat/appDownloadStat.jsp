<%@page import="net.sf.json.JSONObject"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.pixshow.framework.utils.DateUtility"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");
String code = (String) result.get("code");
List<Map<String, Object>> data = (List<Map<String, Object>>) result.get("data");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
</head>
<body>
<form action="<%=basePath%>manager/appDownloadStat.do" method="get">
	<input type="hidden" name="code" value="<%=code%>">
	<table id="customers">
		<tr>
			<td>
				<select name="monthNum">
					<option value="1">最近一个月</option>
					<option value="2">最近两个月</option>
					<option value="3">最近三个月</option>
					<option value="4">最近四个月</option>
					<option value="5">最近五个月</option>
					<option value="6">最近六个月</option>
				</select>
			</td>
			<td><button type="submit">查询</button></td>
		</tr>
	</table>
</form>
	<table id="customers">
		<tr>
			<th>日期</th>
	    	<th>总量</th>
		</tr>
	<%for(int i=0;i<data.size();i++) {
	    Map<String, Object> count = data.get(i);
	%>
		<tr>
			<td><%=count.get("day")%></td>
			<td><%=count.get("count")%></td>
		</tr>
	<%} %>
	</table>
	<br />
</body>
</html>