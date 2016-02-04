<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.pixshow.framework.utils.StringUtility"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%
String basePath = WebUtility.getBasePath(request);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>login</title>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
  </head>
  
  <body>
	<div align="center" style="font-style: red; height: 20px;">${message}</div>
	<form action="<%=basePath%>login.do" method="post">
	<input name="url" type="hidden" value="<%=StringUtility.isEmpty(request.getParameter("url"))?"":request.getParameter("url") %>">
	  <table id="customers" align="center" width="300px">
	  	<tr>
			<th>用户</th>
			<td><input type="text" name="username"></td>  	
	  	</tr>
	  	<tr>
			<th>密码</th>
			<td><input type="password" name="password"></td>  	
	  	</tr>
	  	<tr>
			<td colspan="2"><input type="submit" value="登录"></td>  	
	  	</tr>
	  </table>
	</form>
  </body>
</html>
