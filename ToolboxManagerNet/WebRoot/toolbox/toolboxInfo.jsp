<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.pixshow.toolboxmgr.tools.ImageStorageTootl"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
JSONObject data = (JSONObject) request.getAttribute("result");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
  </head>
  
  <body>
  <form action="<%=basePath %>manager/toolboxInfoUpdate.do" enctype="multipart/form-data" method="post">
	<table id="customers" width="98%">
		<tr>
			<th>标题</th>
			<td>
				中文：<input type="text" name="chTitle" value="<%=data.optString("chTitle") %>" size="50"><br/>
				英文：<input type="text" name="enTitle" value="<%=data.optString("enTitle") %>" size="50">
			</td>
		</tr>
		<tr>
			<th>副标题</th>
			<td>
				中文：<input type="text" name="subChTitle" value="<%=data.optString("subChTitle") %>" size="50"><br/>
				英文：<input type="text" name="subEnTitle" value="<%=data.optString("subEnTitle") %>" size="50">
			</td>
		</tr>
		<tr>
			<th>原图片</th>
			<td><img width="200" height="200" src="<%=data.optString("icon") + "?" + System.currentTimeMillis()%>"></td>
		</tr>
		<tr>
			<th>新图片</th>
			<td><input type="file" name="icon"></td>
		</tr>
		<tr>
			<th colspan="2"><button>提   交</button></th>
		</tr>
	</table>
  </form>
  </body>
</html>
