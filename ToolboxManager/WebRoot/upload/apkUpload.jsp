<%@page import="com.pixshow.framework.config.Config"%>
<%@page import="com.pixshow.toolboxmgr.bean.ApkUploadBean"%>
<%@page import="java.util.List"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="java.util.Map"%>
<%@page import="com.pixshow.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String basePath = WebUtility.getBasePath(request);
Map<String, Object> result = (Map<String, Object>) request.getAttribute("result");
List<ApkUploadBean> apkUploads = (List<ApkUploadBean>) result.get("apkUploads");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/table.css">
</head>
<body>
	<form action="<%=basePath%>manager/apkUpload.do" method="post" enctype="multipart/form-data">
		<input type="file" name="apk">
		<button type="submit">上传</button>
		<select name="urlType">
			<option value="cdn">生成CDN链接</option>
			<option value="ori">生成原始链接</option>
		</select>
		<input type="checkbox" name="doNotModifyFileName" value="true"/>不允许修改文件名
	</form>
	<table id="customers">
		<tr>
			<th>文件名称</th>
			<th>下载链接</th>
			<th>时间</th>
		</tr>
		
<%for(ApkUploadBean bean : apkUploads) {
	String url = bean.getFileName();
	if(url!=null && !url.matches("^http.*$")){
		url = Config.getInstance().getString("toolbox.apkDownloadUrl")+url;
	}
%>
		<tr>
			<td><a href="<%=url %>"><%=bean.getFileName() %></a></td>
			<td><%=url %></td>
			<td><%=bean.getCreateDate() %></td>
		</tr>
<%}%>
	</table>
</body>
</html>