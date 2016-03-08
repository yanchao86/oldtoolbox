<%@page import="com.pixshow.apkpack.bean.ApkChannelBean"%>
<%@page import="com.pixshow.apkpack.bean.ApkKeyBean"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");
List<ApkChannelBean> apkChannels = (List<ApkChannelBean>) result.get("apkChannels");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>渠道维护</title>
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
<script type="text/javascript">
	function delChannel(id) {
		
	}
</script>
</head>
<body>
渠道维护
<form action="<%=basePath%>manager/apkChannel.do" method="post">
<table id="customers">
	<tr>
		<th>名称</th>
		<td><input type="text" name="name"></td>
	</tr>
	<tr>
		<th>channel</th>
		<td><input type="text" name="channel"></td>
	</tr>
	<tr>
		<td colspan="2"><button type="submit">提  交</button></td>
	</tr>
</table>	
</form>

<table id="customers">
	<tr>
		<th>序号</th>
		<th>名称</th>
		<th>channel</th>
		<th>创建时间</th>
		<th>操作</th>
	</tr>
<%for(ApkChannelBean product : apkChannels) {%>
	<tr>
		<td><%=product.getId() %></td>
		<td><%=product.getName() %></td>
		<td><%=product.getChannel() %></td>
		<td><%=product.getCreateDate() %></td>
		<td><a href="javascript:delChannel(<%=product.getId()%>)">删除</a></td>
	</tr>
<%}%>
</table>
</body>
</html>