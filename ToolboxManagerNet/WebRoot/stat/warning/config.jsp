<%@page import="net.sf.json.JSONObject"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.pixshow.toolboxmgr.bean.StatWarningBean"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
String basePath = WebUtility.getBasePath(request);
JSONArray statWarnings = (JSONArray) request.getAttribute("result");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.10.1.min.js" language="JavaScript"></script>
<script type="text/javascript">
	function deleteStatWaring(code) {
		if(confirm("确定删除么")) {
			window.location.href="<%=basePath%>manager/deleteStatWaring.do?code="+code;
		}
	}
</script>
</head>
<body>
<form action="<%=basePath%>manager/saveStatWaring.do" method="post">
	<table id="customers">
		<tr >
			<th>code</th>
			<th>email</th>
			<th>添加</th>
		</tr>
		<tr>
			<td><input type="text" name="code"></td>
			<td><textarea rows="3" cols="20" name="emails"></textarea></td>
			<td><button type="submit">添  加</button> </td>
		</tr>
	</table>
</form>

	<table id="customers">
		<tr>
			<th>序号</th>
			<th>名称</th>
			<th>code</th>
			<th>email</th>
			<th>操作</th>
		</tr>
<%for(int i=0;i<statWarnings.size();i++) {
JSONObject bean = statWarnings.getJSONObject(i);%>

<form method="post" action="<%=basePath%>manager/updaeStatWaring.do">
<input type="hidden" value="<%=bean.optString("code")%>" name="code">
		<tr>
	 		<td><%=bean.optInt("id") %></td>	   
	 		<td><%=bean.optString("codeName") %></td>	   
	 		<td><%=bean.optString("code") %></td>	   
	 		<td>
	 			<textarea rows="3" cols="20" name="emails"><%=bean.optString("email") %></textarea>
	 		</td>
	 		<td>
	 			<button type="submit">修  改</button>
	 			<button type="button" onclick="deleteStatWaring('<%=bean.optString("code")%>')">删  除</button>
	 		</td>
		</tr>
</form>	
<%}%>
	</table>
</body>
</html>