<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
<script type="text/javascript">
	$(function() {
		var allTalbleHtml = [];
		$.post('<%=basePath%>manager/gridList.do', {}, function(data) {
			var gridList = data.data.gridList;
			for(var grid in gridList) {
				allTalbleHtml.push("<tr>");
				allTalbleHtml.push("<td>","<a href='<%=basePath%>manager/gridInfo.do?code=",gridList[grid].code,"'>",gridList[grid].code,"</a>","</td>");
				allTalbleHtml.push("<td>",gridList[grid].name,"</td>");
				allTalbleHtml.push("<td>",gridList[grid].createDate,"</td>");
				allTalbleHtml.push("<td><a href='<%=basePath%>manager/gridDataPage.do?code=",gridList[grid].code,"'>查看</a></td>");
				allTalbleHtml.push("</tr>");
			}
			$(allTalbleHtml.join("")).appendTo("#customers");
		});
	});
 </script>	
</head>
<body>
	<table id="customers">
		<tr>
			<th>表名</th>
			<th>描述</th>
			<th>创建时间</th>
			<th>操作</th>
		</tr>
	</table>
</body>
</html>