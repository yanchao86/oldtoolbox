<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");
List<Map<String, Object>> stats = new ArrayList<Map<String, Object>>(); 
if(result != null) {
	stats = (List<Map<String, Object>>) result.get("stats");
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
	<script type="text/javascript">
		
	</script>
</head>
<body>
<form action="<%=basePath %>manager/statByCode.do" method="post">
	<table id="customers">
		<tr>
			<td><input type="text" name="keyCode"></td>
			<td><button>查询</button></td>		
		</tr>
	</table>
</form>

	<table id="customers">
		<tr>
			<th>code</th>
			<th>name</th>
			<th>day</th>
			<th>count</th>
		</tr>
	<%for(Map<String, Object> map : stats) {%>
		<tr>
			<td><%=map.get("code") %></td>
			<td><%=map.get("name") %></td>
			<td><%=map.get("day") %></td>
			<td><%=map.get("count") %></td>
		</tr>
	<%} %>
	</table>
</body>
</html>